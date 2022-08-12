package tiago.cognizant.technicaltest

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tiago.cognizant.technicaltest.databinding.ActivityMainBinding
import tiago.cognizant.technicaltest.view.ContactListAdapter


class MainActivity : AppCompatActivity() {
    private val numberOfContactsToRead = 500
    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MyViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.url.text = viewModel.generateURL()

        val mContactRecycler = instantiateRecyclerView()
        loadContacts()

        viewModel.contacts.observe(this) {
            val mMessageAdapter = ContactListAdapter(it)
            mContactRecycler.adapter = mMessageAdapter
        }

        binding.genURLButton.setOnClickListener{
            binding.url.text = viewModel.generateURL()
        }

        binding.sendContacts.setOnClickListener{
            Log.d("Contacts","Activity: send Contacts")
            //viewModel.sendContacts(this)
            viewModel.sendPackageName(this)
        }

        startService(Intent(this, NotificationListener::class.java))


    }

    //Check if app has READ_CONTACTS permission, if not have yet, will request it otherwise will load the contacts
    private fun loadContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                //Request read contacts permission
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                numberOfContactsToRead)
        } else {
            viewModel.loadContacts(this)
        }
    }

    //Redefinition of the callback to try load the contacts again if the permission was granted
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == numberOfContactsToRead) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                //Permission not granted after request
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