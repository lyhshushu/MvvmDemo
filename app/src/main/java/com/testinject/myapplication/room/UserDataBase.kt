package com.testinject.myapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.testinject.myapplication.User

@Database(version = 1, entities = [User::class])
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var instance: UserDataBase? = null

        /**
         * 单例话数据库，同步避免多线程写入数据库
         */
        @Synchronized
        fun getDatabase(context: Context): UserDataBase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                UserDataBase::class.java,
                "user_database"
            )
                .allowMainThreadQueries()
                .build().apply {
                    instance = this
                }
        }
    }

}