package com.example.kotlincomposedemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DemoFlowViewModel : ViewModel() {

    // 使用 StateFlow 来管理数据流
    private val _data = MutableStateFlow("Initial Data")
    val data: StateFlow<String> get() = _data

    // 更新数据的方法
    fun updateData(newData: String) {
        _data.value = newData
    }
}