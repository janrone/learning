package com.example.kotlincomposedemo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kotlincomposedemo.databinding.ActivityDemoMainXmlBinding
import com.example.kotlincomposedemo.databinding.ActivityMainDemoBinding

class MainDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()


        val binding = ActivityMainDemoBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvOne.setOnClickListener {
            Toast.makeText( this,"Click binding.tvOne", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            // 设置进入和退出动画
            overridePendingTransition(R.anim.slide_in_up, R.anim.no_animation);

        }
    }
}