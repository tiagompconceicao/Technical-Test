package tiago.cognizant.technicaltest

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tiago.cognizant.technicaltest.databinding.ActivityMainBinding
import tiago.cognizant.technicaltest.view.ContactListAdapter


class MainActivity : AppCompatActivity() {
    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MyViewModel by viewModels()
    private var notificationID = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.url.setText(viewModel.generateURL())

        val mContactRecycler = instantiateRecyclerView()
        loadContacts()

        if (!isNotificationServiceEnabled()){
            Log.d("Notifications","Not enabled")
        }

        viewModel.contacts.observe(this) {
            val mMessageAdapter = ContactListAdapter(it)
            mContactRecycler.adapter = mMessageAdapter
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
            viewModel.sendPackageName(this)
        }

        binding.sendNotifications.setOnClickListener{
            createNotificationChannel()

            val builder = NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationID, builder.build())
            }

            notificationID++
        }

        startService(Intent(this, NotificationListener::class.java))


    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "channel_name"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("0", name, importance).apply {
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun isNotificationServiceEnabled(): Boolean {
        return if (checkSelfPermission(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            //Request read contacts permission
            requestPermissions(arrayOf(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE), 0)

            false
        } else {
            true
        }
    }

    //Check if app has READ_CONTACTS permission, if not have yet, will request it otherwise will load the contacts
    private fun loadContacts() {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                //Request read contacts permission
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                1)
        } else {
            viewModel.loadContacts(this)
        }
    }

    //Redefinition of the callback to try load the contacts again if the permission was granted
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            0 -> {
                Log.d("Notifications", grantResults[0].toString())
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Notifications","Permission acquired")
                } else {
                    //Permission not granted after request
                }
            }

            1 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadContacts()
                } else {
                    //Permission not granted after request
                }
            }
        }
    }

    private fun instantiateRecyclerView(): RecyclerView {
        val mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.reverseLayout = false
        val mContactRecycler = binding.recyclerView
        mContactRecycler.layoutManager = mLayoutManager
        return mContactRecycler
    }
}