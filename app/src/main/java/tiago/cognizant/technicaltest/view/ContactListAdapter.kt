package tiago.cognizant.technicaltest.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tiago.cognizant.technicaltest.R
import tiago.cognizant.technicaltest.model.Contact

/**
 * Adapter Class to build and instantiate the View object of the contact list for the recycler view
 */
class ContactListAdapter(contactsList: List<Contact>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val contactsList = contactsList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.contacts_list_item, parent, false)
        return ContactHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contact: Contact = contactsList[position]
        return (holder as ContactHolder?)!!.bind(contact)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    private inner class ContactHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var nameText: TextView = itemView.findViewById(R.id.contact_name)
        var phoneText: TextView = itemView.findViewById(R.id.contact_phone)
        fun bind(contact: Contact) {
            nameText.text = contact.name
            // Format the stored timestamp into a readable String using method.
            phoneText.text = contact.phoneNumber

        }
    }

}