package ch.subri.morninglight.data.services

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import ch.subri.morninglight.data.TAG

class BoundServiceWrapper {
    private var messenger: Messenger? = null
    private var bound: Boolean = false
    private var context: Context? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (service == null) {
                return
            }

            messenger = Messenger(service)
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            messenger = null
            bound = false
        }
    }

    fun bind(context: Context, intent: Intent) {
        this.context = context

        // Explicitly create service with startService
        context.startService(Intent(context, BLEService::class.java))
        context.bindService(intent, connection, Context.BIND_ABOVE_CLIENT)
    }

    fun unbind() {
        if (bound) {
            context?.unbindService(connection)
            bound = false
            context = null
        }
    }

    fun send(msg: Message): Boolean {
        try {
            messenger?.send(msg)
        } catch (e: RemoteException) {
            Log.w(TAG, e.stackTraceToString())
            return false
        }

        return true
    }

    val isBound: Boolean = bound
}