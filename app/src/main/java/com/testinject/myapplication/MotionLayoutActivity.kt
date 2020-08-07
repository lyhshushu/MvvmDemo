package com.testinject.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import kotlinx.android.synthetic.main.activity_motion.*

class MotionLayoutActivity : AppCompatActivity() {

    private var data: IMyAidlInterface? = null
    private var mBound = false
    var list = ArrayList<String>()
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion)
        addDataBtn.setOnClickListener {
            data!!.setData("data" + i)
            i++
        }
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

}
