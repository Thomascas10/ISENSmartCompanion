package fr.isen.casimir.isensmartcompanion

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import fr.isen.casimir.isensmartcompanion.models.Event
import fr.isen.casimir.isensmartcompanion.NotificationReceiver

const val CHANNEL_ID = "event_notifications"

fun scheduleNotification(context: Context, event: Event) {
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("event_title", event.title)
    }

    val requestCode = event.id.toIntOrNull() ?: event.hashCode()

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val triggerTime = System.currentTimeMillis() + 10_000  // 10 secondes après

    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
}

fun cancelNotification(context: Context, event: Event) {
    val intent = Intent(context, NotificationReceiver::class.java)
    val requestCode = event.id.toIntOrNull() ?: event.hashCode()
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Event Notifications"
        val descriptionText = "Notifications for pinned events"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
            enableLights(true)
            lightColor = Color.BLUE
            enableVibration(true)
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
