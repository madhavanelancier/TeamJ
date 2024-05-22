package com.elancier.team_j

import android.R
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.util.Log


class TestApplication : Application() {

    public val CAHNNEL_ID = "HEADS_UP_NOTIFICATION"
    override fun onCreate() {

        super.onCreate()
        createNotificationChannel()
        Thread.setDefaultUncaughtExceptionHandler { thread, ex -> handleUncaughtException(thread, ex) }

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CAHNNEL_ID, "HEADS_UP_NOTIFICATION", importance)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun handleUncaughtException(thread: Thread, e: Throwable) {

        val stackTrace = Log.getStackTraceString(e)

        val message = e.message


        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "message/rfc822"

        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("muthuelancier@gmail.com"))

        intent.putExtra(Intent.EXTRA_SUBJECT, "Vanitha  log file")

        intent.putExtra(Intent.EXTRA_TEXT, stackTrace)

        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK // required when starting from Application


        startActivity(Intent.createChooser(intent, "Send error report to developer..."))


    }

}