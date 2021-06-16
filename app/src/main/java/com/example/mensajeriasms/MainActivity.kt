package com.example.mensajeriasms

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.abs


class MainActivity : AppCompatActivity() {
    private val REQUESTCODE = 12
    private lateinit var recyclerView: RecyclerView
    private lateinit var floating: FloatingActionButton
    private lateinit var searchMessage: EditText
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var list: MutableList<SmsModel>

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycleView)
        recyclerView.setHasFixedSize(true)
        relativeLayout = findViewById(R.id.layoutFindMessage)
        floating = findViewById(R.id.floating)
        searchMessage = findViewById(R.id.findMessage)

        floating.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, ShowMessagesActivity::class.java)
            )
        }
        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    relativeLayout.apply {
                        visibility = View.GONE
                        animate().alpha(1f)
                            .duration = 200
                    }
                }
                if (dy < 0) {
                    relativeLayout.apply {
                        visibility = View.VISIBLE
                        animate()
                            .alpha(1f)
                            .duration = 200
                    }
                    relativeLayout.visibility = View.VISIBLE
                }
            }
        })

        permission()
        searchMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()) {
                    val new = list.distinctBy { it._address }
                        .filter { it._folderName == "inbox" }
                    val adapter = SmsAdapter(
                        applicationContext,
                        new
                    )
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(
                        applicationContext,
                        RecyclerView.VERTICAL,
                        false
                    )
                } else {
                    val new = list.distinctBy { it._address }
                        .filter { it._folderName == "inbox" }
                    val auxLis = new.filter {
                        it._address.contains(s, true)
                    }
                    setupRecycleView(auxLis)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun permission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS),
                REQUESTCODE
            )
        } else {
            myMessages()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun myMessages() {
        list = mutableListOf()
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
                list.add(objSms)
                c.moveToNext()
            }
        }
        val new = list.distinctBy { it._address }
            .filter { it._folderName == "inbox" }
        setupRecycleView(new)
    }

    private fun setupRecycleView(list: List<SmsModel>) {
        val adapter = SmsAdapter(
            applicationContext,
            list
        )
        adapter.setOnClickListener { view ->
            val number = view.findViewById<TextView>(R.id.phoneNumber)
            startActivity(
                Intent(
                    applicationContext,
                    ShowMessagesActivity::class.java
                )
                    .apply {
                        putExtra("number", number.text)
                    }
            )
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.VERTICAL,
            false
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUESTCODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myMessages()
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS),
                    REQUESTCODE
                )
            }
        }
    }
}