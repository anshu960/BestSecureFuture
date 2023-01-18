package com.darkness.sparkwomen

import android.Manifest
import com.google.android.gms.location.FusedLocationProviderClient
import android.content.Intent
import android.os.IBinder
import android.annotation.SuppressLint
import com.darkness.sparkwomen.ServiceMine
import android.media.MediaPlayer
import com.darkness.sparkwomen.R
import com.google.android.gms.location.LocationServices
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import com.google.android.gms.tasks.OnSuccessListener
import com.github.tbouron.shakedetector.library.ShakeDetector
import com.github.tbouron.shakedetector.library.ShakeDetector.OnShakeListener
import android.content.SharedPreferences
import com.darkness.sparkwomen.SmsActivity
import android.app.PendingIntent
import android.os.Build
import android.app.NotificationChannel
import android.app.NotificationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.darkness.sparkwomen.MainActivity
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import com.darkness.sparkwomen.Terms_and_Condition
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.darkness.sparkwomen.MyOnClickListener
import androidx.recyclerview.widget.RecyclerView
import com.darkness.sparkwomen.ContactsAdapter.MyViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView

class SmsActivity : AppCompatActivity() {
  private lateinit var start: Button
   private lateinit var stop: Button
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@SmsActivity, MainActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms)
        stop = findViewById(R.id.stopService)
        start = findViewById(R.id.startService)
        start.setOnClickListener(View.OnClickListener { view: View? -> startServiceV(view) })
        stop.setOnClickListener(View.OnClickListener { view: View? -> this.stopService(view) })
    }

    fun stopService(view: View?) {
        val notificationIntent = Intent(this, ServiceMine::class.java)
        notificationIntent.action = "stop"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ServiceMine.Companion.isRunning) {
                applicationContext.startForegroundService(notificationIntent)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Service Stopped!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } else {
            if (ServiceMine.Companion.isRunning) {
//                getApplicationContext().startService(notificationIntent);
                applicationContext.startService(notificationIntent)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Service Stopped!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    fun startServiceV(view: View?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val notificationIntent = Intent(this, ServiceMine::class.java)
            notificationIntent.action = "Start"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                applicationContext.startForegroundService(notificationIntent)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Service Started!",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                applicationContext.startService(notificationIntent)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Service Started!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}