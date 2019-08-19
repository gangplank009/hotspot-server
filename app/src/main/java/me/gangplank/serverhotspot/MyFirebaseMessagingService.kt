package me.gangplank.serverhotspot

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val tag = "FMS"
    private val subscribeTo = "SERVER"
    private val adminChannelId = "admin_channel"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        val intent = Intent(this@MyFirebaseMessagingService, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt(1000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            setupChannels(notificationManager)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this@MyFirebaseMessagingService,
            0, intent, PendingIntent.FLAG_ONE_SHOT
        )

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.notify_icon)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            this@MyFirebaseMessagingService, adminChannelId
        ).apply {
            setSmallIcon(R.drawable.notify_icon)
            setLargeIcon(largeIcon)
            setContentTitle(remoteMessage?.data?.get("title"))
            setContentText(remoteMessage?.data?.get("text"))
            setAutoCancel(true)
            setSound(notificationSound)
            setContentIntent(pendingIntent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            notificationBuilder.color = resources.getColor(R.color.colorPrimary)

        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager) {
        val adminChannelName = "New Notification"
        val adminChannelDescription  = "Device to device notification"

        val adminChannel = NotificationChannel(adminChannelId, adminChannelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = adminChannelDescription
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(adminChannel)
    }

    override fun onNewToken(p0: String?) {
        val token = FirebaseInstanceId.getInstance().getToken()

        FirebaseMessaging.getInstance().subscribeToTopic(subscribeTo)
        Log.d(tag, "onNewToken completed with token $token")
    }


}
