package com.example.kotlincomposedemo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainViewXmlActivity : AppCompatActivity() {
    private val TAG = "MainViewXmlActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_view_xml)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var btn = findViewById<Button>(R.id.btnFenXi)
        val visualizer = findViewById<AudioSegmentVisualizer>(R.id.segmentVisualizer)

        btn.setOnClickListener {
            Log.d(TAG, "=== 开始音频处理流程 ===")
            try {
                // 1. 初始化TFLite模型
                Log.d(TAG, "1. 初始化TensorFlow Lite模型")
                val model = TFLiteModel(this)
                
                // 2. 开始处理音频文件
                Log.d(TAG, "2. 开始处理音频文件")
                val segments = AudioSegmentProcessor.processAudioFile(resources, R.raw.test_audio, model)
                
                // 3. 输出分段结果
                Log.d(TAG, "3. 处理完成，输出分段结果：")
                for (segment in segments) {
                    Log.d(TAG, "时间段: ${segment.startTime}s 到 ${segment.endTime}s, 类别: ${segment.predictedClass}")
                }

                // 4. 更新可视化结果
                Log.d(TAG, "4. 更新可视化显示")
                visualizer.setSegments(segments)
                
                Log.d(TAG, "=== 音频处理流程结束 ===")
            } catch (e: Exception) {
                Log.e(TAG, "处理过程中发生错误", e)
            }
        }
    }
}