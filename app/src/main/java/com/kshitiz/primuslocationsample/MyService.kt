package com.kshitiz.primuslocationsample

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.google.android.gms.location.*


class MyService : Service() {


    companion object{
      lateinit  var instance:MyService
    }
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    override fun onCreate() {
        super.onCreate()

        instance = this
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        restartServiceIntent.setPackage(packageName)
        startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)

    }
    override fun onDestroy() {
        super.onDestroy()
       // instance = null!!;
        if (mFusedLocationClient != null)
            mFusedLocationClient!!.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent): IBinder {
        return null!!
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        locationRequest = LocationRequest.create()
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest!!.setInterval(0); // Update location every 2 sec
//        locationRequest!!.setFastestInterval(0);
        locationRequest!!.setSmallestDisplacement(50.0f);
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        var wayLatitude = location.latitude
                        var wayLongitude = location.longitude

                        Toast.makeText(this@MyService, "${wayLatitude},${wayLongitude}", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }



        mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, null)



        return START_STICKY
    }


}
