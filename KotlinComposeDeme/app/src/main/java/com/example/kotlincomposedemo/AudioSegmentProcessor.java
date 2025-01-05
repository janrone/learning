package com.example.kotlincomposedemo;

import android.content.res.Resources;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class AudioSegmentProcessor {
    private static final String TAG = "AudioSegmentProcessor";
    private static final float STEP_SIZE = 0.18f;  // 步长（秒）
    private static final float DURATION = 0.18f;   // 每段持续时间（秒）
    private static final int SAMPLE_RATE = 22050;  // 采样率
    
    public static class Segment {
        public float startTime;
        public float endTime;
        public int predictedClass;
        
        public Segment(float startTime, float endTime, int predictedClass) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.predictedClass = predictedClass;
        }
    }
    
    /**
     * 计算总时长（秒）
     */
    private static float getTotalDuration(float[][] melSpectrogram) {
        if (melSpectrogram == null || melSpectrogram.length == 0) {
            return 0f;
        }
        // 假设每帧代表 DURATION 秒的音频
        return melSpectrogram.length * DURATION;
    }
    
    /**
     * 从梅尔频谱图中提取指定时间窗口的特征
     */
    private static float[][] extractFeatureWindow(float[][] melSpectrogram, float startTime, float duration) {
        int startFrame = (int) (startTime / DURATION);
        int numFrames = (int) (duration / DURATION);
        
        // 确保不超出边界
        startFrame = Math.max(0, Math.min(startFrame, melSpectrogram.length - 1));
        numFrames = Math.min(numFrames, melSpectrogram.length - startFrame);
        
        float[][] window = new float[numFrames][melSpectrogram[0].length];
        for (int i = 0; i < numFrames; i++) {
            System.arraycopy(melSpectrogram[startFrame + i], 0, window[i], 0, melSpectrogram[0].length);
        }
        
        return window;
    }
    
    /**
     * 获取预测类别
     */
    private static int getPredictedClass(float[] predictions) {
        if (predictions == null || predictions.length == 0) {
            throw new IllegalArgumentException("Predictions array is empty or null");
        }
        
        // 找到最大值的索引
        int maxIndex = 0;
        float maxValue = predictions[0];
        for (int i = 1; i < predictions.length; i++) {
            if (predictions[i] > maxValue) {
                maxValue = predictions[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
    
    /**
     * 处理音频文件并返回分段结果
     */
    public static List<Segment> processAudioFile(Resources resources, int rawResourceId, 
                                               TFLiteModel model) {
        Log.d(TAG, "=== 开始音频分段处理 ===");
        final List<Segment> segments = new ArrayList<>();
        
        try {
            Log.d(TAG, "1. 开始读取音频文件并提取梅尔频谱图");
            MelSpectrogramUtil.processAudioFileAsync(resources, rawResourceId, 
                new MelSpectrogramUtil.MelSpectrogramCallback() {
                    @Override
                    public void onProcessed(float[][] melSpectrogram) {
                        Log.d(TAG, "2. 梅尔频谱图提取完成，开始分段处理");
                        if (melSpectrogram == null || melSpectrogram.length == 0) {
                            Log.e(TAG, "错误：获取到空的梅尔频谱图");
                            return;
                        }

                        float currentTime = 0;
                        float totalDuration = getTotalDuration(melSpectrogram);
                        Log.d(TAG, String.format("总时长: %.2f秒", totalDuration));
                        
                        int windowCount = 0;
                        while (currentTime < totalDuration) {
                            windowCount++;
                            Log.d(TAG, String.format("处理第%d个时间窗口 (%.2f秒)", windowCount, currentTime));
                            
                            // 提取特征
                            float[][] features = extractFeatureWindow(melSpectrogram, currentTime, DURATION);
                            float[] mask = AudioFeatureExtractor.createMask(features);
                            
                            // 执行推理
                            float[] predictions = model.runInference(features, mask);
                            int predictedClass = getPredictedClass(predictions);
                            
                            Log.d(TAG, String.format("时间窗口%d预测结果：类别%d", windowCount, predictedClass));
                            
                            segments.add(new Segment(currentTime, currentTime + DURATION, predictedClass));
                            currentTime += STEP_SIZE;
                        }
                        
                        Log.d(TAG, String.format("共处理了%d个时间窗口", windowCount));
                    }
                    
                    @Override
                    public void onProgress(float progress) {
                        Log.d(TAG, String.format("处理进度: %.1f%%", progress * 100));
                    }
                    
                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "处理过程中发生错误", e);
                    }
                });
            
            Log.d(TAG, "3. 开始合并相邻的相同类别片段");
            List<Segment> mergedSegments = mergeSegments(segments);
            Log.d(TAG, String.format("合并后的片段数量: %d", mergedSegments.size()));
            
            return mergedSegments;
            
        } catch (Exception e) {
            Log.e(TAG, "音频处理过程中发生错误", e);
            throw new RuntimeException("Failed to process audio file", e);
        }
    }
    
    /**
     * 合并相同类别的连续片段
     */
    private static List<Segment> mergeSegments(List<Segment> segments) {
        if (segments.isEmpty()) return segments;
        
        List<Segment> mergedSegments = new ArrayList<>();
        Segment currentSegment = segments.get(0);
        
        for (int i = 1; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            if (segment.predictedClass == currentSegment.predictedClass) {
                // 扩展当前片段
                currentSegment.endTime = segment.endTime;
            } else {
                // 保存当前片段并开始新片段
                mergedSegments.add(currentSegment);
                currentSegment = segment;
            }
        }
        
        // 添加最后一个片段
        mergedSegments.add(currentSegment);
        
        return mergedSegments;
    }
    
    // 其他辅助方法...
} 