package com.testinject.myapplication

class NewsModel {

    var netString: String = "this is data from model"

    fun loadDataFromNet(): String {
        return netString
    }

    fun changeData() {
        netString = "have changed"
    }
}