package com.example.kotlincomposedemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DemoViewModel : ViewModel() {

    // 创建一个可变的 LiveData，用来更新数据
    private val _data = MutableLiveData<String>()
    
    // 创建一个只读的 LiveData，外部只能观察，不可修改
    val data: LiveData<String> get() = _data

    // 模拟改变数据的函数
    fun updateData(newData: String) {
        _data.value = newData
    }
}