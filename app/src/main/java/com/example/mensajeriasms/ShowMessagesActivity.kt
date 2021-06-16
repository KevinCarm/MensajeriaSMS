package com.example.mensajeriasms

import android.content.ContentResolver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class ShowMessagesActivity : AppCompatActivity() {
    private var number: String? = null
    private lateinit var btnSend: ImageView
    private lateinit var message: EditText
    private lateinit var listMessages: MutableList<SmsModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_messages)

        number = intent.extras?.getString("number")
        setup()
        getMessages()
    }

    private fun setup() {
        listMessages = mutableListOf()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        btnSend = findViewById(R.id.btnSend)
        message = findViewById(R.id.message)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Messages"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun getMessages() {
        var objSms: SmsModel
        val message = Uri.parse("content://sms/")
        val cr: ContentResolver = contentResolver
        val c = cr.query(message, null, null, null, null)
        startManagingCursor(c)
        val totalSMS = c!!.count

        if (c.moveToFirst()) {
            for (i in 0 until totalSMS) {
                objSms = SmsModel()
                objSms._id = (c.getString(c.getColumnIndexOrThrow("_id")))
                objSms._address = (c.getString(c.getColumnIndexOrThrow("address")))
                objSms._sms = c.getString(c.getColumnIndexOrThrow("body"))
                objSms._readState = (c.getString(c.getColumnIndex("read")))
                objSms._time = (c.getString(c.getColumnIndexOrThrow("date")))
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms._folderName = ("inbox")
                } else {
                    objSms._folderName = ("sent")
                }
                listMessages.add(objSms)
                c.moveToNext()
            }
            c.close()
            setupRecycleView(listMessages)
        }
    }

    private fun setupRecycleView(list: List<SmsModel>) {
        val auxNumber: String
        if (number?.contains("+52")!!) {
            auxNumber = number?.substring(3, number?.length!!)!!

            list.filter { it._address == number || it._address == auxNumber }
                .forEach {
                    println("--->> $it")
                }
        } else {

        }
    }
}