package com.example.kotlincomposedemo


import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.kotlincomposedemo.viewmodel.DemoFlowViewModel
import com.example.kotlincomposedemo.viewmodel.DemoViewModel
import kotlinx.coroutines.launch

class ViewModelFlowActivity : AppCompatActivity() {

    // 使用 by viewModels() 委托来获取 ViewModel 实例
    private val myViewModel: DemoFlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView: TextView = findViewById(R.id.textView)
        val updateButton: Button = findViewById(R.id.updateButton)


        // 使用 lifecycleScope 来收集 Flow 数据
        lifecycleScope.launch {
            // 观察 Flow，数据变化时更新 UI
            myViewModel.data.collect { newData ->
                // 数据变化时，更新 UI
                textView.text = newData
            }
        }

        // 按钮点击时更新 Flow 数据
        updateButton.setOnClickListener {
            myViewModel.updateData("Hello, Flow! Updated!")
        }
    }
}
