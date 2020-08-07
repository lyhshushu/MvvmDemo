package com.testinject.myapplication

class NewsModel {

    private var netString: String = "this is data from model"

    fun loadDataFromNet(): String {
        return netString
    }

    fun changeData() {
        netString = if (netString == "this is data from model") {
            "have changed"
        } else {
            "this is data from model"
        }
    }

}