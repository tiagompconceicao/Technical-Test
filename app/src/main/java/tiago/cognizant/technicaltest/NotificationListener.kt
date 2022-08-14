package tiago.cognizant.technicaltest

import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log


/**
 * Add-on #1
 * Notification listener service
 */
class NotificationListener : NotificationListenerService() {

    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

        override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("Notifications","Listener connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Implement what you want here
        Log.d("Notifications","Notification received")
        val intent = Intent("tiago.cognizant.technicaltest.notificationlistener")
        intent.putExtra("Notification", sbn)
        sendBroadcast(intent)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Implement what you want here
        Log.d("Notifications","Notification removed")
    }
}