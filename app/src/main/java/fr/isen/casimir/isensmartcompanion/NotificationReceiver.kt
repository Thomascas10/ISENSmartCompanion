package fr.isen.casimir.isensmartcompanion

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.isen.casimir.isensmartcompanion.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val eventTitle = intent?.getStringExtra("eventTitle") ?: "Événement"

        val notification = context?.let {
            NotificationCompat.Builder(it, "event_notifications")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Rappel d'Événement")
                .setContentText("N'oubliez pas : $eventTitle")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
        }

        context?.let {
            NotificationManagerCompat.from(it).notify(1, notification!!)
        }
    }
}
