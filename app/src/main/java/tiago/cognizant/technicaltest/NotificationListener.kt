package tiago.cognizant.technicaltest

import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Implement what you want here
        Log.d("Notifications","Notification received")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Implement what you want here
        Log.d("Notifications","Notification removed")
    }
}