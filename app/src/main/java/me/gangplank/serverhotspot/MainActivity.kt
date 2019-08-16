package me.gangplank.serverhotspot

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.FileReader

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

/*        mChannel = manager?.initialize(this@MainActivity, mainLooper, null)
        mChannel?.also { channel ->
            mReceiver = WiFiDirectBroadcastReceiver(manager!!, channel, this@MainActivity)
        }

        manager?.createGroup(mChannel, object: WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d(LOG_TAG, "Group created")
            }

            override fun onFailure(reason: Int) {
                Log.d(LOG_TAG, "Group not created")
            }
        })*/
    }

    private fun getClients(): String {
        var macCount = 0
        val br = BufferedReader(FileReader("/proc/net/arp"))
        try {
            val iterator = br.lineSequence().iterator()
            val stringBuilder = StringBuilder()
            iterator.next()
            while (iterator.hasNext()) {
                val line = iterator.next()
                val splitted = line.split(Regex(" +"))
                if (splitted.isNotEmpty()) {
                    val mac = splitted[3]
                    val ipAddr = splitted[0]
                    if (mac.matches(Regex("..:..:..:..:..:.."))) {
                        macCount++
                        Log.d(LOG_TAG, "Mac : $mac IP Address : $ipAddr")
                        Log.d(LOG_TAG, "Mac_Count : $macCount MAC_ADDRESS : $mac")
                        stringBuilder.append("Mac_Count : $macCount\n")
                            .append("MAC_ADDRESS : $mac\n")
                            .append("IP Address : $$ipAddr")
                            .append("Status : $macCount\n")
                    }
                }
            }
            br.close()
            return stringBuilder.toString()

        } catch (e: Exception) {
            Log.d(LOG_TAG, e.message)
            br.close()
            return "Connected devices not found"
        }
    }

    override fun onResume() {
        super.onResume()

        val devStr = getClients()
        connected_devices_text_view.text = devStr

/*        registerReceiver(mReceiver, intentFilter)

        manager?.requestGroupInfo(mChannel) {
            it?.let {
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
        }*/
    }

    override fun onPause() {
        super.onPause()
        //     unregisterReceiver(mReceiver)
    }
}
