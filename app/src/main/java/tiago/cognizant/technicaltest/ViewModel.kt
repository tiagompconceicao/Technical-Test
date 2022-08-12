package tiago.cognizant.technicaltest

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tiago.cognizant.technicaltest.model.Contact
import tiago.cognizant.technicaltest.model.URL
import tiago.cognizant.technicaltest.repo.Repository
import java.util.*
import kotlin.reflect.full.functions


class MyViewModel(app: Application) : AndroidViewModel(app) {

    val contacts: LiveData<List<Contact>> = MutableLiveData()

    private val repository by lazy {
        Repository()
    }


    private var url: URL = URL()

    fun generateURL(): String {
        url = URL()
        return url.getURL()
    }

    fun loadContacts(mContext: Context){
        repository.getContacts(mContext, onSuccess = {
            (contacts as MutableLiveData<List<Contact>>).value = it
        }, onError = {
            Toast.makeText(mContext, "Could not get contact list", Toast.LENGTH_LONG).show()
        })
    }

    fun sendContacts(mContext: Context){
        Log.d("Contacts","View Model: send Contacts")
        repository.sendRequest(mContext,url.getURL(), contacts.value!!,"contacts")
    }

    //Add-on #3
    //Method that gets the application package name and encodes it to base64 using reflection
    private fun encodePackageName(mContext: Context): String{

        //Get application package name
        val contextMethods = mContext::class.functions
        val getPackageName = contextMethods.find{it.name == "getPackageName"}
        val packageName: String = getPackageName?.call(mContext) as String
        val byteArray = packageName.toByteArray()

        //Get method to encode string to base64
        val getEncoder = Base64::class.functions.find { it.name == "getEncoder" }
        val encoder = getEncoder?.call()
        val encodeToString = encoder!!::class.functions.find { it.name == "encodeToString" }
        val encoded: String = encodeToString?.call(encoder,byteArray) as String


        Log.d("Package","Coding finished")
        Log.d("Package",encoded)

        return encoded
    }

    //Add-on #3 (cont.)
    //Send the encoded name to a given URL
    fun sendPackageName(mContext: Context){
        val encoded = encodePackageName(mContext)
        repository.sendRequest(mContext,url.getURL(),encoded,"packageName")
    }
}