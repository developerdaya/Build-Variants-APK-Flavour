package com.developerdaya.demonotification



import com.google.firebase.messaging.FirebaseMessagingService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import java.lang.ref.WeakReference

class FirebaseMessageReceiver : FirebaseMessagingService() {
    private val contextRef: WeakReference<Context> = WeakReference(this)

    override fun onNewToken(token: String) {
       // SharedPreferenceUtility.getInstance(this).deviceToken = token
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        Log.d("#########", "onMessageReceived: $p0")
        if (p0 != null) {
            if (p0.notification != null) {
                val title = p0.notification?.title
                val body = p0.notification?.body
                val type = p0.data["notificationType"]
                val intent = Intent("custom-event-name")
                // if (!LocalBroadcastManager.getInstance(this).sendBroadcast(intent))
                sendNotificationData(contextRef.get(), body, title, type, p0)
                // LocalBroadcastManager.getInstance(this).sendBroadcast(intent)


            } else {
                sendNotificationData(contextRef.get(), "hello", resources.getString(R.string.app_name), "0", null)
            }

        }
    }

    private fun sendNotificationData(
        context: Context?,
        body: String?,
        title: String?,
        type: String?,
        p0Value: RemoteMessage?
    ) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context!!, "channelId")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channelId",
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}




