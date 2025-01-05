package com.example.kotlincomposedemo;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.mfcc.MFCC;

public class AudioFeatureExtractor {
    private static final String TAG = "AudioFeatureExtractor";
    private static final int SR = 22050;  // 采样率
    private static final int N_MFCC = 13; // MFCC特征数
    private static final int TARGET_LENGTH = 100; // 目标序列长度
    
    /**
     * 提取MFCC特征
     */
    public static float[][] extractFeatures(float[] audioData, int sampleRate) {
        try {
            // 创建音频格式
            TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(sampleRate, 16, 1, true, false);
            
            // 创建MFCC处理器
            int bufferSize = 2048; // 帧大小
            int hopSize = bufferSize / 2; // 帧移
            MFCC mfcc = new MFCC(bufferSize, sampleRate, N_MFCC, 0, sampleRate/2f, 0.97f);
            
            // 存储MFCC特征
            List<float[]> featuresList = new ArrayList<>();
            
            // 处理音频数据
            for (int offset = 0; offset + bufferSize <= audioData.length; offset += hopSize) {
                // 准备当前帧的数据
                float[] buffer = new float[bufferSize];
                System.arraycopy(audioData, offset, buffer, 0, bufferSize);
                
                // 创建AudioEvent并设置数据
                AudioEvent audioEvent = new AudioEvent(format);
                audioEvent.setFloatBuffer(buffer);
                
                // 处理当前帧
                mfcc.process(audioEvent);
                
                // 获取MFCC系数并存储
                float[] mfccs = mfcc.getMFCC();
                if (mfccs != null) {
                    featuresList.add(mfccs.clone());
                }
            }
            
            // 检查是否提取到了特征
            if (featuresList.isEmpty()) {
                throw new RuntimeException("No MFCC features extracted from audio data");
            }
            
            // 转换List为二维数组
            float[][] features = new float[featuresList.size()][];
            for (int i = 0; i < featuresList.size(); i++) {
                features[i] = featuresList.get(i);
            }
            
            Log.d(TAG, "Extracted " + features.length + " MFCC frames");
            
            // 填充或截断到目标长度
            return padOrTruncateFeatures(features, TARGET_LENGTH);
            
        } catch (Exception e) {
            Log.e(TAG, "Error extracting MFCC features", e);
            throw new RuntimeException("Failed to extract MFCC features", e);
        }
    }
    
    /**
     * 填充或截断特征到指定长度
     */
    private static float[][] padOrTruncateFeatures(float[][] features, int targetLength) {
        if (features == null || features.length == 0) {
            throw new IllegalArgumentException("Features array is empty or null");
        }
        
        float[][] result = new float[targetLength][features[0].length];
        
        // 如果特征长度大于目标长度，则截断
        if (features.length > targetLength) {
            for (int i = 0; i < targetLength; i++) {
                System.arraycopy(features[i], 0, result[i], 0, features[0].length);
            }
        }
        // 如果特征长度小于目标长度，则填充
        else {
            // 复制现有特征
            for (int i = 0; i < features.length; i++) {
                System.arraycopy(features[i], 0, result[i], 0, features[0].length);
            }
            // 用0填充剩余部分
            for (int i = features.length; i < targetLength; i++) {
                for (int j = 0; j < features[0].length; j++) {
                    result[i][j] = 0.0f;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 创建特征掩码
     */
    public static float[] createMask(float[][] features) {
        float[] mask = new float[features.length];
        for (int i = 0; i < features.length; i++) {
            float sum = 0;
            for (float value : features[i]) {
                sum += value;
            }
            mask[i] = sum != 0 ? 1.0f : 0.0f;
        }
        return mask;
    }
} 