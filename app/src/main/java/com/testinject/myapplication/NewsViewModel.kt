package com.testinject.myapplication

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.*

class NewsViewModel(timeCount: Int, saveNetString: String) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

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
        uiScope.launch {
            delay(1000)
            //切换执行位置
            withContext(Dispatchers.Main) {
                userLiveData.value = Repository.getUser(userId).value
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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
        viewModelScope.launch {
            _time.value = 1
            _liveData.value = mModel.loadDataFromNet() + _time.value
        }
    }

}