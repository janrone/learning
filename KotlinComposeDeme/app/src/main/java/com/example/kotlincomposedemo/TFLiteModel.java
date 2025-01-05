package com.example.kotlincomposedemo;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import android.content.res.AssetFileDescriptor;


public class TFLiteModel {
    private static final String TAG = "TFLiteModel";

    private Interpreter tflite;
    private int outputSize;
    private static final int TARGET_LENGTH = 100;
    private static final int N_MFCC = 13;

    public TFLiteModel(Context context) throws Exception {
        Log.d(TAG, "=== 初始化TensorFlow Lite模型 ===");
        try {
            Log.d(TAG, "1. 加载模型文件");
            MappedByteBuffer modelBuffer = loadModelFile(context);
            
            Log.d(TAG, "2. 创建Interpreter实例");
            Interpreter.Options options = new Interpreter.Options();
            tflite = new Interpreter(modelBuffer, options);
            
            // 检查输入张量
            int numInputs = tflite.getInputTensorCount();
            Log.d(TAG, "Number of input tensors: " + numInputs);
            if (numInputs != 2) {
                throw new RuntimeException("Model must have 2 inputs, but found " + numInputs);
            }

            // 检查输入张量的形状
            Tensor inputTensor0 = tflite.getInputTensor(0);
            Tensor inputTensor1 = tflite.getInputTensor(1);
            Log.d(TAG, "Input 0 shape: " + Arrays.toString(inputTensor0.shape()));
            Log.d(TAG, "Input 1 shape: " + Arrays.toString(inputTensor1.shape()));

            // 检查输出张量
            int numOutputs = tflite.getOutputTensorCount();
            Log.d(TAG, "Number of output tensors: " + numOutputs);
            if (numOutputs != 3) {
                throw new RuntimeException("Model must have 3 outputs, but found " + numOutputs);
            }

            // 检查所有输出张量的形状
            Tensor outputTensor0 = tflite.getOutputTensor(0);
            Tensor outputTensor1 = tflite.getOutputTensor(1);
            Tensor outputTensor2 = tflite.getOutputTensor(2);
            Log.d(TAG, "Output 0 shape: " + Arrays.toString(outputTensor0.shape()));
            Log.d(TAG, "Output 1 shape: " + Arrays.toString(outputTensor1.shape()));
            Log.d(TAG, "Output 2 shape: " + Arrays.toString(outputTensor2.shape()));

            // 设置输出大小
            outputSize = outputTensor0.shape()[1];

            Log.d(TAG, "=== 模型初始化完成 ===");
        } catch (Exception e) {
            Log.e(TAG, "模型初始化失败", e);
            throw e;
        }
    }

    private MappedByteBuffer loadModelFile(Context context) throws Exception {
        try {
            // 从assets文件夹加载模型
            String modelPath = "audio_model.tflite";
            AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelPath);
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            
            Log.d(TAG, "Loading model: " + modelPath);
            Log.d(TAG, "Start offset: " + startOffset);
            Log.d(TAG, "Declared length: " + declaredLength);
            
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
            
            // 关闭资源
            fileDescriptor.close();
            inputStream.close();
            
            return buffer;
        } catch (IOException e) {
            Log.e(TAG, "Error loading model", e);
            throw new RuntimeException("Error loading model: " + e.getMessage());
        }
    }

    // 执行推理
    public float[] runInference(float[][] features, float[] mask) {
        try {
            Log.d(TAG, "开始执行模型推理");
            
            Log.d(TAG, "1. 准备输入数据");
            // 准备输入数据
            ByteBuffer inputBuffer = ByteBuffer.allocateDirect(4 * TARGET_LENGTH * N_MFCC);
            inputBuffer.order(ByteOrder.nativeOrder());
            
            for (float[] feature : features) {
                for (float value : feature) {
                    inputBuffer.putFloat(value);
                }
            }
            
            // 准备掩码数据
            ByteBuffer maskBuffer = ByteBuffer.allocateDirect(4 * TARGET_LENGTH);
            maskBuffer.order(ByteOrder.nativeOrder());
            for (float value : mask) {
                maskBuffer.putFloat(value);
            }
            
            // 准备输入数组
            Object[] inputs = new Object[2];
            inputs[0] = inputBuffer;
            inputs[1] = maskBuffer;
            
            // 准备输出Map
            Map<Integer, Object> outputs = new HashMap<>();
            float[][] classLogits = new float[1][8];  // 假设有8个类别
            float[][] boundaryOutputs = new float[1][1];
            float[][] thirdOutput = new float[1][1];  // 添加第三个输出
            outputs.put(0, classLogits);
            outputs.put(1, boundaryOutputs);
            outputs.put(2, thirdOutput);  // 添加第三个输出到Map中
            
            Log.d(TAG, "2. 执行推理");
            tflite.runForMultipleInputsOutputs(inputs, outputs);
            
            Log.d(TAG, "3. 推理完成，返回结果");
            return (float[]) outputs.get(0);
            
        } catch (Exception e) {
            Log.e(TAG, "模型推理过程中发生错误", e);
            throw new RuntimeException("Error running inference", e);
        }
    }

    public void close() {
        if (tflite != null) {
            tflite.close();
        }
    }
}

