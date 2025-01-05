package com.example.kotlincomposedemo

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlincomposedemo.databinding.ActivityDemoMainXmlBinding

class DemoMainXmlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_demo_main_xml)
        // 初始化 ViewBinding
        val binding = ActivityDemoMainXmlBinding.inflate(layoutInflater)

         //val binding by viewBinding(ActivityDemoMainXmlBinding::bind)

        // 使用 ViewBinding 设置内容视图
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            val spannable = SpannableString("  " + "Kotlin 是 Android 官方推荐的编程语言。")
            val drawable: Drawable = ContextCompat.getDrawable(
                this@DemoMainXmlActivity,
                R.mipmap.ic_title_d
            )!!
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            val imageSpan = ImageSpan(drawable)
            spannable.setSpan(
                imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            tvOne.text = spannable
        }

        binding.apply {
            val spannable = SpannableString("  " + "通过启用 ViewBinding 和使用自动生成的 Binding 类，你可以避免传统的 findViewById，让代码更简洁且类型安全。binding.main 用来直接访问布局中的视图，避免了查找视图的麻烦，使代码更加易于维护。")
            val drawable: Drawable = ContextCompat.getDrawable(
                this@DemoMainXmlActivity,
                R.mipmap.ic_title_s
            )!!
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            val imageSpan = ImageSpan(drawable)
            spannable.setSpan(
                imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            tvTwo.text = spannable
        }



    }
}