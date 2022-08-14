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

    /**
     * Callback function that is called when a notification is posted
     * When this function is called, the notification is sent to the MainActivity by the broadcast channel
     */
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d("Notifications","Notification received")

        val intent = Intent("tiago.cognizant.technicaltest.notificationlistener")
        intent.putExtra("Notification", sbn)
        sendBroadcast(intent)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("Notifications","Notification removed")
    }
}