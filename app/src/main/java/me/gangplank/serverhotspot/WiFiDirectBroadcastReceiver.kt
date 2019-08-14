package me.gangplank.serverhotspot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import android.widget.Toast

const val LOG_TAG = "LOG_DEBUG"

class WiFiDirectBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val activity: MainActivity
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action: String = intent?.action ?: ""

        when (action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent?.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)

                when (state) {
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED -> {
                        // WiFi is enabled
                        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {

                            override fun onSuccess() {
                                Toast.makeText(activity, "Peers discovered", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(reason: Int) {
                                Toast.makeText(activity, "Peers not discovered($reason)", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    else -> {
                        // WiFi is not enabled
                        Toast.makeText(activity, "Enable WiFi", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                manager.requestPeers(channel) {

                    val stringBuilder = StringBuilder()

                    for (item in it.deviceList) {

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

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {

            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {

            }
        }
    }
}