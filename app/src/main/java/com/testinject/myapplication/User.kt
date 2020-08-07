package com.testinject.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(var firstName: String, var lastName: String, var age: Int) {
    //主键设置
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}