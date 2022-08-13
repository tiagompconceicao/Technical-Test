package tiago.cognizant.technicaltest.view

import android.service.notification.StatusBarNotification
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tiago.cognizant.technicaltest.R
import tiago.cognizant.technicaltest.databinding.ActivityMainBinding
import java.text.SimpleDateFormat

class NotificationListAdapter(notificationList: List<StatusBarNotification>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val notificationList = notificationList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.notifications_list_item, parent, false)
        return NotificationHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notification: StatusBarNotification = notificationList[position]
        return (holder as NotificationHolder?)!!.bind(notification)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    private inner class NotificationHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.not_title)
        var timestamp: TextView = itemView.findViewById(R.id.not_timestamp)
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")

        fun bind(notification: StatusBarNotification) {
            title.text = notification.packageName
            // Format the stored timestamp into a readable String using method.
            timestamp.text = simpleDateFormat.format(notification.notification.`when`)
        }
    }

}