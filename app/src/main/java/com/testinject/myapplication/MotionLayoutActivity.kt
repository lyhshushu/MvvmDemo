package com.testinject.myapplication

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_motion.*
import java.lang.Exception

class MotionLayoutActivity : AppCompatActivity() {

    private var data: IMyAidlInterface? = null
    private var mBound = false
    var list = ArrayList<String>()
    private var i = 0
    private var context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion)
        addDataBtn.setOnClickListener {
            data!!.setData("data" + i)
            i++
        }
        hookOnClickListener(addDataBtn)
        loadDataBtn.setOnClickListener {
            list = data!!.data as ArrayList<String>
            var sb = StringBuffer()
            for (d in list) {
                sb.append(d).append("\n")
            }
            dataTv.text = sb
        }
    }

    override fun onStart() {
        attemptToBindService()
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        unbindService(mServiceConnection)
    }

    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            mBound = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            data = IMyAidlInterface.Stub.asInterface(service)
            mBound = true
            if (data != null) {
                try {
                    list = data!!.data as ArrayList<String>
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun attemptToBindService() {
        val intent = Intent()
        intent.action = "com.text.getdata.data"
        intent.`package` = "com.testinject.myapplication"
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }

    @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
    private fun hookOnClickListener(v: View?) {
        try {
            //得到view的ListenerInfo对象
            val getListenerInfo = View::class.java.getDeclaredMethod("getListenerInfo")
            getListenerInfo.isAccessible = true
            val listenerInfo = getListenerInfo.invoke(v)
            //得到原始的OnClickListener对象
            val listenerClazz = Class.forName("android.view.View\$ListenerInfo")
            val mOnClickListener = listenerClazz.getDeclaredField("mOnClickListener")
            mOnClickListener.isAccessible = true
            val originOnClickListener = mOnClickListener.get(listenerInfo) as View.OnClickListener
            //使用自定义的OnclickListener替换
            val hookedOnClickListener = HookedOnClickListener(originOnClickListener, context)
            mOnClickListener.set(listenerInfo, hookedOnClickListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    class HookedOnClickListener(
        private var origin: View.OnClickListener,
        private var context: Context
    ) : View.OnClickListener {
        override fun onClick(v: View?) {
            Toast.makeText(context, "this is hook click", Toast.LENGTH_SHORT)
                .show()
            Log.d("MotionLayout", "Before click, do what you want to to.")
            origin.onClick(v)
            Log.d("Motionlayout", "After click, do what you want to to.")
        }

    }

}
