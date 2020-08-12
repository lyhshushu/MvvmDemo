package com.testinject.myapplication

import androidx.annotation.IntegerRes
import androidx.lifecycle.*
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.properties.Delegates

class NewsViewModel(timeCount: Int, saveNetString: String) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    //第一次get之前执行，可添加操作
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

    suspend fun plusTime() {
        val timeNow = _time.value ?: 1
        _time.value = timeNow + 1;
        mModel.changeData()
        _liveData.value = mModel.loadDataFromNet() + _time.value
        val request = request()
        println(request)
    }

    fun clear() {
        viewModelScope.launch {
            _time.value = 1
            _liveData.value = mModel.loadDataFromNet() + _time.value
        }
    }

    //接口回调代码优化
    private suspend fun request(): String {
        return suspendCoroutine { continuation ->
            //回调实现直接使用continuation提取出返回数据
            continuation.resume("hahaha")
        }
    }

    //方法地址直接调用
    suspend fun getRequest() {
        try {
            val response = request()
            //直接处理数据
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //gc以及finalize使用
    class TestGC() {
        protected fun finalize() {
            println("我已经被垃圾回收器回收了...");
        }

        public fun main(args: String) {
            val gc: TestGC?
            gc = null
            System.gc()
        }
    }


}