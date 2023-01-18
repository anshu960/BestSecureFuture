package com.darkness.sparkwomen

import android.Manifest
import com.google.android.gms.location.FusedLocationProviderClient
import android.content.Intent
import android.os.IBinder
import android.annotation.SuppressLint
import android.app.*
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
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.darkness.sparkwomen.MainActivity
import android.os.Bundle
import android.telephony.SmsManager
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
import android.widget.TextView
import java.util.HashSet

class ServiceMine : Service() {
    var fusedLocationClient: FusedLocationProviderClient? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    var manager = SmsManager.getDefault()

    //    WindowManager windowManager;
    var myLocation: String? = null
    var view: View? = null
    private lateinit var mediaPlayer: MediaPlayer
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(baseContext, R.raw.siren)
        mediaPlayer.setLooping(true)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
            .addOnSuccessListener { location ->
                myLocation = if (location != null) {
                    // Logic to handle location object
                    location.altitude
                    location.longitude
                    "http://maps.google.com/maps?q=loc:" + location.latitude + "," + location.longitude
                } else {
                    "Unable to Find Location :("
                }
            }
        ShakeDetector.create(this) {
            mediaPlayer.start()
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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent.action.equals("STOP", ignoreCase = true)) {
            if (isRunning) {
                mediaPlayer!!.stop()
                this.stopForeground(true)
                this.stopSelf()
                //                    windowManager.removeView(view);
                isRunning = false
            }
        } else {
            val notificationIntent = Intent(this, SmsActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "MYID",
                    "CHANNELFOREGROUND",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                val m = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                m.createNotificationChannel(channel)
                val notification = Notification.Builder(this, "MYID")
                    .setContentTitle("Best Secure Future")
                    .setContentText("Shake Device to Send SOS")
                    .setSmallIcon(R.drawable.splashscreen)
                    .setContentIntent(pendingIntent)
                    .build()
                this.startForeground(115, notification)
                isRunning = true
                return START_NOT_STICKY
            }
            if (intent.action.equals("PLAY", ignoreCase = true)) {
                mediaPlayer!!.start()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
        super.onDestroy()
    }

    companion object {
        var isRunning = false
        var mediaPlayer: MediaPlayer? = null
    }
}