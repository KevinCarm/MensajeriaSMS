package com.example.mensajeriasms

import android.os.Bundle
import android.provider.ContactsContract
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SelectContactActivity : AppCompatActivity() {
    private lateinit var recycleView: RecyclerView
    private lateinit var listContact: MutableList<ContactModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_contact)
        recycleView = findViewById(R.id.recycleView)
        listContact = mutableListOf()
        getContacts()
    }

    private fun getContacts() {
        val cr = contentResolver
        val cur = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        while (cur!!.moveToNext()) {
            val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
            val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

            val pCur = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null
            )
            while (pCur!!.moveToNext()) {
                val phone = pCur.getString(
                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                )
                listContact.add(
                    ContactModel(
                        id,
                        name,
                        phone
                    )
                )
            }
            pCur.close()
        }
        cur.close()
        listContact.sortByDescending {
            it.name
        }
        listContact.reverse()
        val adapter = ContactAdapter(
            applicationContext,
            listContact
        )
        adapter.setOnClickListener {
            val name = it.findViewById<TextView>(R.id.contactName)
            Toast.makeText(applicationContext, "Name ${name.text}", Toast.LENGTH_SHORT).show()
        }
        recycleView.adapter = adapter
        recycleView.layoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.VERTICAL,
            false
        )
    }
}