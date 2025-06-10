package com.example.lab5_task2
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainLayout: LinearLayout
    private var isAirplaneModeOn = false
    private var isCharging = false

    private val powerConnectedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            isCharging = intent.action == Intent.ACTION_POWER_CONNECTED
            if (isCharging) {
                Toast.makeText(context, "Підключено!", Toast.LENGTH_SHORT).show()
            }
            updateBackground()
        }
    }

    private val powerDisconnectedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            isCharging = false
            updateBackground()
        }
    }

    private val airplaneModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            isAirplaneModeOn = intent.getBooleanExtra("state", false)
            val airplaneModeText = findViewById<TextView>(R.id.airplaneModeText)
            airplaneModeText.text = "${if (isAirplaneModeOn) "Ви в літачку" else "Ви більше не в літачку"}"
            updateBackground()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.mainLayout)

        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
            registerReceiver(null, ifilter)
        }
        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
        isAirplaneModeOn = android.provider.Settings.Global.getInt(
            contentResolver,
            android.provider.Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
        val airplaneModeText = findViewById<TextView>(R.id.airplaneModeText)
        airplaneModeText.text = "Airplane Mode: ${if (isAirplaneModeOn) "On" else "Off"}"
        updateBackground()
    }

    override fun onStart() {
        super.onStart()
        val powerConnectedFilter = IntentFilter(Intent.ACTION_POWER_CONNECTED)
        registerReceiver(powerConnectedReceiver, powerConnectedFilter)
        val powerDisconnectedFilter = IntentFilter(Intent.ACTION_POWER_DISCONNECTED)
        registerReceiver(powerDisconnectedReceiver, powerDisconnectedFilter)
        val airplaneFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, airplaneFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(powerConnectedReceiver)
        unregisterReceiver(powerDisconnectedReceiver)
        unregisterReceiver(airplaneModeReceiver)
    }

    private fun updateBackground() {
        when {
            isAirplaneModeOn -> {
                mainLayout.setBackgroundColor(Color.parseColor("#32A8E6"))
            }
            isCharging -> {
                mainLayout.setBackgroundColor(Color.parseColor("#28C76F"))
            }
            else -> {
                mainLayout.setBackgroundColor(Color.parseColor("#EA5455"))
            }
        }
    }
}