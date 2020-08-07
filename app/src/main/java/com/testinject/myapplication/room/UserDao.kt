package com.testinject.myapplication.room

import androidx.room.*
import com.testinject.myapplication.User

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User): Long

    @Delete
    fun deleteUser(user: User)

    @Update
    fun updateUser(newUser: User)

    @Query("select * from User")
    fun loadAllUser(): List<User>

    @Query("select * from User where age>:age")
    fun loadUserOlderThan(age: Int): List<User>

    @Query("delete from User where lastName=:lastName")
    fun deleteUserByLastName(lastName: String): Int
}