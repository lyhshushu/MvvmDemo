package com.testinject.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.activity_main.*


const val word: String = "all place use"

class MainActivity : AppCompatActivity() {

    private lateinit var newsVm: NewsViewModel
    private lateinit var sp: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        editText = findViewById(R.id.ev)
//        textView = findViewById(R.id.tv)

        //不可新建，viewModel拥有独立的生命周期
        sp = getPreferences(Context.MODE_PRIVATE)
        val time = sp.getInt("time_count", 0)
        val netString = sp.getString("net_string", "")
        lifecycle.addObserver(MyObserve(lifecycle))

        newsVm =
            ViewModelProviders.of(this, NewsViewModelFactory(time, netString.toString()))
                .get(NewsViewModel::class.java)

        tv.setOnClickListener {
            newsVm.plusTime();
            Toast.makeText(this, "jjj", Toast.LENGTH_SHORT).show()
        }
        bt_next.setOnClickListener {
            var intent: Intent = Intent(this, MotionLayoutActivity::class.java)
            startActivity(intent);
        }
        clearBtn.setOnClickListener {
//            newsVm.clear()
            val userId = (0..10000).random().toString()
            newsVm.getUser(userId)
        }

        initLiveData()
        println(getViewId(ev))
        ev.hint = getViewId(ev)
        tv.text = word
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
    }

    override fun onPause() {
        sp.edit {
            putInt("time_count", newsVm.time.value ?: 1)
            putString("net_string", newsVm.liveData.value ?: "")
        }
        super.onPause()
    }

}


