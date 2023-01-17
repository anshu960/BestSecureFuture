package com.darkness.sparkwomen

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import android.os.Bundle
import com.google.android.gms.location.LocationServices
import android.content.Intent
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.telephony.SmsManager
import android.view.View
import java.util.HashSet

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var fusedLocationClient: FusedLocationProviderClient? = null
    var myLocation = ""
    var numberCall: String? = null
    var manager = SmsManager.getDefault()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        findViewById<View>(R.id.panicBtn).setOnClickListener(this)
        findViewById<View>(R.id.first).setOnClickListener(this)
        findViewById<View>(R.id.second).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id
        // if (id == R.id.fourth) {
        if (id == R.id.first) {
            startActivity(Intent(this@MainActivity, ContactActivity::class.java))
            finish()
        } else if (id == R.id.second) {
            startActivity(Intent(this@MainActivity, SmsActivity::class.java))
            finish()
        } else if (id == R.id.panicBtn) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationClient!!.lastLocation
                .addOnSuccessListener { location: Location? ->
                    myLocation = if (location != null) {
                        location.altitude
                        location.longitude
                        "http://maps.google.com/maps?q=loc:" + location.latitude + "," + location.longitude
                    } else {
                        "Unable to Find Location :("
                    }
                    sendMsg()
                }
            val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
            numberCall = sharedPreferences.getString("firstNumber", "None")
            if (!numberCall.equals("None", ignoreCase = true)) {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$numberCall")
                startActivity(intent)
            }
        }
    }

    fun sendMsg() {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val oldNumbers = sharedPreferences.getStringSet("enumbers", HashSet())
        if (!oldNumbers!!.isEmpty()) {
            for (ENUM in oldNumbers) manager.sendTextMessage(
                ENUM,
                null,
                "Im in Trouble!\nSending My Location :\n$myLocation",
                null,
                null
            )
        }
    }
}