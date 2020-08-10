package com.testinject.myapplication.aidlinterface

import android.util.Log
import com.testinject.myapplication.IMyAidlInterface
import com.testinject.myapplication.User

class MyInterfaceData : IMyAidlInterface.Stub() {

    var list = ArrayList<String>()

    override fun getData(): MutableList<String> {
        return list
    }

    override fun setData(s: String) {
        Log.e("dataAidl", s)
        list.add(s)
    }

    override fun getUser(): User {
        TODO("Not yet implemented")
    }

    override fun SetUser(user: User?) {
        TODO("Not yet implemented")
    }

}