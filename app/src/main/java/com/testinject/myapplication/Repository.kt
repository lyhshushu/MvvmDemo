package com.testinject.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object Repository {
    fun getUser(userId: String): LiveData<User> {
        val liveData = MutableLiveData<User>()
        liveData.value = User(userId, userId, 10)
        return liveData
    }
}