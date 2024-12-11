package com.example.kotlincomposedemo


import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.kotlincomposedemo.viewmodel.DemoViewModel

class ViewModelActivity : AppCompatActivity() {

    // 使用 by viewModels() 委托来获取 ViewModel 实例
    private val myViewModel: DemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView: TextView = findViewById(R.id.textView)
        val updateButton: Button = findViewById(R.id.updateButton)


        // 观察 LiveData，数据变化时更新 UI
        myViewModel.data.observe(this, Observer { newData ->
            // 数据变化时，更新 UI
            textView.text = newData
        })

        // 按钮点击时更新 LiveData
        updateButton.setOnClickListener {
            myViewModel.updateData("Hello, LiveData! Updated!")
        }
    }
}
