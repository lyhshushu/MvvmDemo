package com.testinject.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewsViewModel(timeCount: Int) : ViewModel() {

    private val mModel by lazy {
        NewsModel()
    }
    val time: LiveData<Int>
        get() = _time
    val liveData: LiveData<String>
        get() = _liveData


    private val _time = MutableLiveData<Int>()

    private val _liveData = MutableLiveData<String>()

    init {
        _time.value = timeCount
        _liveData.value = mModel.loadDataFromNet() + _time.value
    }

    fun plusTime() {
        val timeNow = _time.value ?: 1
        _time.value = timeNow + 1;
        mModel.changeData()
        _liveData.value = mModel.loadDataFromNet() + _time.value
    }

    fun clear() {
        _time.value = 1
        _liveData.value = mModel.loadDataFromNet() + _time.value
    }

}