package ch.subri.morninglight.data.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import ch.subri.morninglight.data.api.ble.BLEApi
import ch.subri.morninglight.data.api.ble.BLEApiImpl
import ch.subri.morninglight.data.api.preferences.Preferences
import ch.subri.morninglight.data.db.AlarmDao
import ch.subri.morninglight.data.entity.Alarm
import ch.subri.morninglight.data.entity.NightLightSetting
import ch.subri.morninglight.data.entity.toBLEAlarms
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class BLEService : Service() {
    private val managerScope = CoroutineScope(Dispatchers.Default)
    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val scopeCancellationPeriod = 10000L

    private lateinit var messenger: Messenger

    @Inject
    lateinit var alarmDao: AlarmDao

    @Inject
    lateinit var preferences: Preferences

    private var bleApi: BLEApi? = null

    internal class IncomingHandler(
        context: Context,
    ) : Handler() {
        override fun handleMessage(msg: Message) {
            // Add message parsing here
            super.handleMessage(msg)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bleApi = null

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder {
        if (bleApi == null) {
            bleApi = BLEApiImpl.instance
        }

        alarmDao.all()
            .onEach { sendAlarms(it) }
            .launchIn(ioScope)

        preferences.nightLightSetting
            .onEach { it?.let { sendNightLightService(it) } }
            .launchIn(ioScope)

        messenger = Messenger(IncomingHandler(this))
        return messenger.binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        managerScope.launch {
            delay(scopeCancellationPeriod)
            ioScope.cancel()
            stopSelf()
        }

        return super.onUnbind(intent)
    }

    private fun sendAlarms(alarms: List<Alarm>) = alarms
        .toBLEAlarms()
        .onEach { ioScope.launch { bleApi?.setAlarm(it) } }

    private fun sendNightLightService(settings: NightLightSetting) =
        ioScope.launch { bleApi?.setNightLightConfiguration(settings) }
}