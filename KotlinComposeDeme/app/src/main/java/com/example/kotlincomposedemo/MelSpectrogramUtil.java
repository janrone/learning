package com.example.kotlincomposedemo;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import be.tarsos.dsp.mfcc.MFCC;
import be.tarsos.dsp.util.fft.FFT;

/**
 * 音频处理工具类，用于生成梅尔频谱图
 */
public class MelSpectrogramUtil {
    private static final String TAG = "MelSpectrogramUtil";
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * 配置参数类
     */
    public static class Config {
        public final int fftSize;
        public final int melBins;
        public final int hopSize;
        public final float sampleRate;

        public Config(int fftSize, int melBins, int hopSize, float sampleRate) {
            this.fftSize = fftSize;
            this.melBins = melBins;
            this.hopSize = hopSize;
            this.sampleRate = sampleRate;
        }

        public static Config getDefaultConfig(float sampleRate) {
            return new Config(1024, 128, 512, sampleRate);
        }
    }

    /**
     * 处理结果回调接口
     */
    public interface MelSpectrogramCallback {
        void onProcessed(float[][] melSpectrogram);
        void onProgress(float progress);
        void onError(Exception e);
    }

    /**
     * 异步处理音频文件
     */
    public static void processAudioFileAsync(Resources resources, int rawResourceId, 
                                           MelSpectrogramCallback callback) {
        Log.d(TAG, "=== 开始提取梅尔频谱图 ===");
        executor.execute(() -> {
            try {
                Log.d(TAG, "1. 初始化音频提取器");
                MediaExtractor extractor = new MediaExtractor();
                AssetFileDescriptor afd = resources.openRawResourceFd(rawResourceId);
                
                Log.d(TAG, "2. 设置音频数据源");
                extractor.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                
                Log.d(TAG, "3. 获取音频格式信息");
                MediaFormat format = getAudioFormat(extractor);
                if (format == null) {
                    throw new IOException("No audio track found");
                }

                int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                int channels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                Log.d(TAG, String.format("音频信息 - 采样率: %d Hz, 声道数: %d", sampleRate, channels));

                // ... 其他处理代码 ...
                
                Log.d(TAG, "=== 梅尔频谱图提取完成 ===");
            } catch (Exception e) {
                Log.e(TAG, "梅尔频谱图提取过程中发生错误", e);
                notifyError(callback, e);
            }
        });
    }

    /**
     * 处理音频文件
     */
    private static void processAudioFile(Resources resources, int rawResourceId, 
                                       MelSpectrogramCallback callback) throws IOException {
        MediaExtractor extractor = null;
        AssetFileDescriptor afd = null;
        TarsosDSPAudioInputStream tarsosStream = null;

        try {
            extractor = new MediaExtractor();
            afd = resources.openRawResourceFd(rawResourceId);
            
            if (afd == null) {
                throw new IOException("Could not open resource: " + rawResourceId);
            }
            
            extractor.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            
            // 获取音频格式
            MediaFormat format = getAudioFormat(extractor);
            if (format == null) {
                throw new IOException("No audio track found in resource: " + rawResourceId);
            }

            // 获取音频参数
            int sampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            int channels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            
            Log.d(TAG, "Audio format - Sample rate: " + sampleRate + ", Channels: " + channels);

            // 创建配置
            Config config = Config.getDefaultConfig(sampleRate);

            // 创建处理组件
            FFT fft = new FFT(config.fftSize);
            float[] window = createHanningWindow(config.fftSize);
            MFCC mfcc = new MFCC(config.fftSize, config.sampleRate, config.melBins, 
                                0, config.sampleRate/2f, 0.97f);

            // 创建音频流
            tarsosStream = createTarsosStream(extractor, sampleRate, channels, config.fftSize);

            // 处理音频数据
            processAudioData(tarsosStream, config, fft, window, mfcc, callback);

        } catch (Exception e) {
            Log.e(TAG, "Error processing audio file", e);
            throw new IOException("Failed to process audio file", e);
        } finally {
            // 释放资源
            if (tarsosStream != null) tarsosStream.close();
            if (extractor != null) extractor.release();
            if (afd != null) afd.close();
        }
    }

    /**
     * 创建汉宁窗
     */
    private static float[] createHanningWindow(int size) {
        float[] window = new float[size];
        for (int i = 0; i < size; i++) {
            window[i] = (float) (0.5 * (1 - Math.cos(2 * Math.PI * i / (size - 1))));
        }
        return window;
    }

    /**
     * 创建 TarsosDSPAudioInputStream
     */
    private static TarsosDSPAudioInputStream createTarsosStream(MediaExtractor extractor, 
            int sampleRate, int channels, int fftSize) {
        return new TarsosDSPAudioInputStream() {
            private final int BUFFER_SIZE = 1024 * 50;
            private ByteBuffer inputBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
            private byte[] tempBuffer = new byte[BUFFER_SIZE];
            private int bytesRemaining = 0;
            private int bufferPosition = 0;

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                try {
                    if (bytesRemaining == 0) {
                        // 检查当前采样位置
                        long sampleTime = extractor.getSampleTime();
                        if (sampleTime == -1) {
                            return -1;  // 没有更多数据
                        }

                        // 确保 inputBuffer 被正确重置
                        inputBuffer.clear();
                        
                        // 读取采样数据
                        int sampleSize = extractor.readSampleData(inputBuffer, 0);
                        Log.d(TAG, "Read sample size: " + sampleSize + " at time: " + sampleTime);
                        
                        if (sampleSize < 0) {
                            return -1;
                        }

                        // 复制数据到临时缓冲区
                        inputBuffer.position(0);
                        inputBuffer.limit(sampleSize);
                        inputBuffer.get(tempBuffer, 0, sampleSize);
                        bytesRemaining = sampleSize;
                        bufferPosition = 0;

                        // 移动到下一个采样
                        boolean hasMore = extractor.advance();
                        if (!hasMore) {
                            Log.d(TAG, "No more samples available");
                        }
                    }

                    // 复制数据到输出缓冲区
                    int bytesToCopy = Math.min(len, bytesRemaining);
                    System.arraycopy(tempBuffer, bufferPosition, b, off, bytesToCopy);
                    bufferPosition += bytesToCopy;
                    bytesRemaining -= bytesToCopy;

                    return bytesToCopy;
                } catch (Exception e) {
                    Log.e(TAG, "Error reading audio data", e);
                    throw new IOException("Failed to read audio data", e);
                }
            }

            @Override
            public long skip(long bytesToSkip) throws IOException {
                long samplesSkipped = 0;
                while (samplesSkipped < bytesToSkip && extractor.advance()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        samplesSkipped += extractor.getSampleSize();
                    }
                }
                return samplesSkipped;
            }

            @Override
            public void close() throws IOException {
                // 资源在外部关闭
            }

            @Override
            public TarsosDSPAudioFormat getFormat() {
                return new TarsosDSPAudioFormat(sampleRate, 16, channels, true, false);
            }

            @Override
            public long getFrameLength() {
                return -1;
            }
        };
    }

    /**
     * 处理音频数据
     */
    private static void processAudioData(TarsosDSPAudioInputStream audioStream, Config config,
                                       FFT fft, float[] window, MFCC mfcc, 
                                       MelSpectrogramCallback callback) throws IOException {
        int bufferSize = Math.max(config.fftSize * 2, 16384); // Ensure buffer is large enough
        byte[] buffer = new byte[bufferSize];
        float[] floatBuffer = new float[config.fftSize];
        float[] fftBuffer = new float[config.fftSize * 2];
        
        int totalFrames = 0;
        int processedFrames = 0;
        
        // 计算总帧数（如果可能）
        if (audioStream.getFrameLength() > 0) {
            totalFrames = (int) (audioStream.getFrameLength() / config.hopSize);
        }

        int bytesRead;
        while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
            // 转换为浮点数据
            for (int i = 0; i < bytesRead/2 && i < config.fftSize; i++) {
                floatBuffer[i] = (short)((buffer[2*i+1] << 8) | (buffer[2*i] & 0xFF)) / 32768.0f;
            }

            // 应用窗函数和执行 STFT
            for (int i = 0; i < config.fftSize; i++) {
                fftBuffer[2*i] = floatBuffer[i] * window[i];
                fftBuffer[2*i + 1] = 0;
            }
            
            fft.forwardTransform(fftBuffer);

            // 创建 AudioEvent
            AudioEvent audioEvent = new AudioEvent(audioStream.getFormat());
            audioEvent.setFloatBuffer(floatBuffer);

            // 计算梅尔频谱
            mfcc.process(audioEvent);
            float[] melSpectrogram = mfcc.getMFCC();

            // 更新进度
            processedFrames++;
            if (totalFrames > 0) {
                final float progress = (float) processedFrames / totalFrames;
                notifyProgress(callback, progress);
            }
        }
    }

    /**
     * 获取音频格式
     */
    private static MediaFormat getAudioFormat(MediaExtractor extractor) {
        int numTracks = extractor.getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            MediaFormat format = extractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            if (mime != null && mime.startsWith("audio/")) {
                extractor.selectTrack(i);
                Log.d(TAG, "Selected audio track: " + i + " with mime type: " + mime);
                return format;
            }
        }
        return null;
    }

    /**
     * 通知进度
     */
    private static void notifyProgress(MelSpectrogramCallback callback, float progress) {
        if (callback != null) {
            mainHandler.post(() -> callback.onProgress(progress));
        }
    }

    /**
     * 通知错误
     */
    private static void notifyError(MelSpectrogramCallback callback, Exception e) {
        if (callback != null) {
            mainHandler.post(() -> callback.onError(e));
        }
    }

    public static void testMediaExtractor(Resources resources, int rawResourceId) {
        MediaExtractor extractor = null;
        AssetFileDescriptor afd = null;
        
        try {
            extractor = new MediaExtractor();
            afd = resources.openRawResourceFd(rawResourceId);
            
            if (afd == null) {
                Log.e(TAG, "Could not open resource: " + rawResourceId);
                return;
            }
            
            // 设置数据源
            extractor.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            
            // 打印轨道信息
            int numTracks = extractor.getTrackCount();
            Log.d(TAG, "Number of tracks: " + numTracks);
            
            // 遍历所有轨道
            for (int i = 0; i < numTracks; i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);
                Log.d(TAG, "Track " + i + " - MIME: " + mime);
                
                if (mime != null && mime.startsWith("audio/")) {
                    // 选择音频轨道
                    extractor.selectTrack(i);
                    
                    // 尝试读取一些数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024 * 500);
                    int sampleCount = 0;
                    
                    while (sampleCount < 10) {  // 只读取前10个样本进行测试
                        buffer.clear();
                        int sampleSize = extractor.readSampleData(buffer, 0);
                        if (sampleSize < 0) {
                            break;
                        }
                        
                        long sampleTime = extractor.getSampleTime();
                        Log.d(TAG, String.format("Sample %d - Size: %d, Time: %d", 
                              sampleCount, sampleSize, sampleTime));
                        
                        extractor.advance();
                        sampleCount++;
                    }
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error testing MediaExtractor", e);
        } finally {
            if (extractor != null) {
                extractor.release();
            }
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing AssetFileDescriptor", e);
                }
            }
        }
    }
}