package me.gangplank.serverhotspot

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    val manager: WifiP2pManager? by lazy {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    var mChannel: WifiP2pManager.Channel? = null
    var mReceiver: WiFiDirectBroadcastReceiver? = null

    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mChannel = manager?.initialize(this@MainActivity, mainLooper, null)
        mChannel?.also { channel ->
            mReceiver = WiFiDirectBroadcastReceiver(manager!!, channel, this@MainActivity)
        }

        manager?.createGroup(mChannel, object: WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Toast.makeText(this@MainActivity, "Group created", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(reason: Int) {
                Toast.makeText(this@MainActivity, "Group not created", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mReceiver, intentFilter)

        manager?.requestGroupInfo(mChannel) {
            val stringBuilder = StringBuilder()

            stringBuilder
                .append("Owner:")
                .append(it.owner.deviceName + "\n")
                .append("Owner MAC:")
                .append(it.owner.deviceAddress + "\n")
                .append("Status:")
                .append(it.owner.status)

            Log.d(LOG_TAG, stringBuilder.toString())
            stringBuilder.clear()

            for (item in it.clientList) {
                stringBuilder
                    .append("MAC address:")
                    .append(item.deviceAddress + "\n")
                    .append("Device Name:")
                    .append(item.deviceName + "\n")
                    .append("Status:")
                    .append(item.status)

                Log.d(LOG_TAG, stringBuilder.toString())
                stringBuilder.clear()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mReceiver)
    }
}
