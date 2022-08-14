package tiago.cognizant.technicaltest

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tiago.cognizant.technicaltest.databinding.ActivityMainBinding
import tiago.cognizant.technicaltest.view.ContactListAdapter
import tiago.cognizant.technicaltest.view.NotificationListAdapter


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MyViewModel by viewModels()
    private lateinit var broadcastReceiver: BroadcastNotificationReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.url.setText(viewModel.generateURL())

        val mContactRecycler = instantiateRecyclerView(binding.recyclerView)
        val mNotificationRecycler = instantiateRecyclerView(binding.recyclerViewNot)
        loadContacts()

        /**
         * When a notification arrives, the list is re-instantiate, triggering the observer
         * When the observer is triggered, the respective view Adapter view is also re-instantiate
         */
        viewModel.notifications.observe(this){
            val mNotificationAdapter = NotificationListAdapter(it)
            mNotificationRecycler.adapter = mNotificationAdapter
        }

        /**
         * When the contacts are loaded, the list is instantiated, triggering the observer
         * When the observer is triggered, the respective view Adapter view is also instantiated
         * This observer is only triggered once in this application
         */
        viewModel.contacts.observe(this) {
            val mContactAdapter = ContactListAdapter(it)
            mContactRecycler.adapter = mContactAdapter
        }

        binding.genURLButton.setOnClickListener{
            binding.url.setText(viewModel.generateURL())
        }

        binding.sendContacts.setOnClickListener{
            Log.d("Contacts","Activity: send Contacts")
            viewModel.sendContacts(this,binding.url.text.toString())
        }

        binding.sendPackageName.setOnClickListener{
            Log.d("Package","Activity: send Package name")
            viewModel.sendPackageName(this,binding.url.text.toString())
        }

        binding.sendNotifications.setOnClickListener{
            Log.d("notifications","Activity: send notifications")
            viewModel.sendNotifications(this,binding.url.text.toString())
        }

        // Register a receiver to tell the MainActivity when a notification has been received
        broadcastReceiver = BroadcastNotificationReceiver(viewModel)
        val intentFilter = IntentFilter()
        intentFilter.addAction("tiago.cognizant.technicaltest.notificationlistener")
        registerReceiver(broadcastReceiver, intentFilter)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    /**
     * Check if app has READ_CONTACTS permission, if not have yet, will request it otherwise will load the contacts
     */
    private fun loadContacts() {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                //Request read contacts permission
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS,Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE),
                0)
        } else {
            Log.d("Contacts","Gonna load contacts")
            viewModel.loadContacts(this)
        }
    }

    /**
     * Redefinition of the callback to try load the contacts again if the permission was granted
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d("Permissions","Permissions result: ${grantResults[0]}, for request: $requestCode")

        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                //Permission not granted after request
            }
        }
    }


    /**
     * Broadcast Receiver.
     * Used to notify the Main Activity when a new notification has arrived
     */
    class BroadcastNotificationReceiver(private val viewModel: MyViewModel) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("Notifications","Notification received in Activity")

            val receivedNotification: StatusBarNotification? = intent.getParcelableExtra("Notification")
            viewModel.addNotification(receivedNotification!!)
        }
    }

    /**
     * Function used to instantiate generically a recyclerView
     */
    private fun instantiateRecyclerView(recyclerView: RecyclerView): RecyclerView {
        val mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.reverseLayout = false
        recyclerView.layoutManager = mLayoutManager
        return recyclerView
    }
}