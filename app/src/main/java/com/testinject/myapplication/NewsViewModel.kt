package com.testinject.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class NewsViewModel(timeCount: Int, saveNetString: String) : ViewModel() {

    private val mModel by lazy {
        NewsModel()
    }
    val time: LiveData<Int>
        get() = _time
    val liveData: LiveData<String>
        get() = _liveData

    val user: LiveData<User>
        get() = Transformations.switchMap(userLiveData) { userLiveData ->
            Repository.getUser(userLiveData.firstName)
        }

    private val userLiveData = MutableLiveData<User>()


    fun getUser(userId: String) {
        userLiveData.value = Repository.getUser(userId).value
    }

    private val _time = MutableLiveData<Int>()

    private val _liveData = MutableLiveData<String>()

    init {
        _time.value = timeCount
        _liveData.value = saveNetString
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