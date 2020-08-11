package com.testinject.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.*
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.testinject.myapplication.room.UserDataBase
import com.testinject.myapplication.workmanager.SimpleWorker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


const val word: String = "all place use"

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var newsVm: NewsViewModel
    private lateinit var sp: SharedPreferences

    private lateinit var componentName1111: ComponentName
    private lateinit var componentNameDefault: ComponentName
    private val dataScope = CoroutineScope(Dispatchers.IO)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var packageName = packageName
        componentName1111 = ComponentName(this, "$packageName.MainActivity1111")
        componentNameDefault = ComponentName(this, "$packageName.MainActivity")

        val userDao = UserDataBase.getDatabase(this).userDao();
        val user1 = User("liu", "shushu", 22)
        val user2 = User("zhuang", "dandan", 22)


        //不可新建，viewModel拥有独立的生命周期
        sp = getPreferences(Context.MODE_PRIVATE)
        val time = sp.getInt("time_count", 0)
        val netString = sp.getString("net_string", "")
        lifecycle.addObserver(MyObserve(lifecycle))
        newsVm =
            ViewModelProviders.of(this, NewsViewModelFactory(time, netString.toString()))
                .get(NewsViewModel::class.java)

        tv.setOnClickListener {
            onChangeIConTo1111()
            newsVm.plusTime()
            Toast.makeText(this, "jjj", Toast.LENGTH_SHORT).show()
        }
        bt_next.setOnClickListener {
            var intent: Intent = Intent(this, MotionLayoutActivity::class.java)
            startActivity(intent);
        }
        clearBtn.setOnClickListener {
            onChangeIconToDefault()
            newsVm.clear()
        }
        getUserBtn.setOnClickListener {
            val userId = (0..10000).random().toString()
            newsVm.getUser(userId)
        }
        addUserBtn.setOnClickListener {
            //只是测试作用，Room中已经拥有自己的调度器，不应再使用
            launch {
                withContext(Dispatchers.IO) {
                    user1.id = userDao.insertUser(user1)
                    user2.id = userDao.insertUser(user2)
                }

            }
        }
        updateUserBtn.setOnClickListener {
            launch {
                withContext(Dispatchers.IO) {
                    user1.age = 1;
                    userDao.updateUser(user1)
                }
            }
        }
        deleteUserBtn.setOnClickListener {
            launch {
                withContext(Dispatchers.IO) {
                    userDao.deleteUserByLastName("dandan")
                }
            }
        }
        queryUserBtn.setOnClickListener {
            launch {
                withContext(Dispatchers.IO) {
                    for (user in userDao.loadAllUser()) {
                        println(user.toString())
                    }
                }
            }
        }
        backWorkBtn.setOnClickListener {
            val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
                .addTag("simple")
                //??不能小于10s
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .build()
            WorkManager.getInstance(this).enqueue(request)
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id)
                .observe(this) { workInfo ->
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        Log.d("MainActivity", "do work success")
                    } else if (workInfo.state == WorkInfo.State.FAILED) {
                        Log.d("MainActivity", "do work failed")
                    }
                }
        }
        initLiveData()
        println(getViewId(ev))
        ev.hint = getViewId(ev)
        tv.text = word

//        比对性能
//        val start = System.currentTimeMillis()
//        runBlocking {
//            repeat(100000) {
//                launch {
//                    println(it)
//                }
//            }
//
//        }
//        val end = System.currentTimeMillis()
//        println((end - start).toString() + "time")
//
//        thread {
//            repeat(100000) {
//                println(it)
//            }
//        }
//        val end1 = System.currentTimeMillis()
//        println((end1 - start).toString() + "hhhh")


        GlobalScope.launch(Dispatchers.Default) {
            Log.d("MainActivity", Thread.currentThread().name)
        }
    }

    private fun getViewId(view: View): String {
        println("id is" + view.id)
        return "id is" + view.id
    }

    private fun initLiveData() {
        newsVm.user.observe(this) { user ->
            tv.text = user.firstName
        }
        newsVm.liveData.observe(this) { liveData ->
            tv.text = liveData
        }
    }

    override fun onDestroy() {
        println("is destory")
        super.onDestroy()
        //cancel 协程
        cancel()
    }

    override fun onPause() {
        sp.edit {
            putInt("time_count", newsVm.time.value ?: 1)
            putString("net_string", newsVm.liveData.value ?: "")
        }
        super.onPause()
    }

    fun onChangeIConTo1111() {
        enableComponent(componentName1111)
        disableComponent(componentNameDefault)
    }

    fun onChangeIconToDefault() {
        enableComponent(componentNameDefault)
        disableComponent(componentName1111)
    }

    fun enableComponent(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    fun disableComponent(componentName: ComponentName) {
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

}


