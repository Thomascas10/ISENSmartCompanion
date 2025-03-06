package fr.isen.casimir.isensmartcompanion.models

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class NotificationViewModel : ViewModel() {

    private var toastHandler: Handler? = null
    private var toastRunnable: Runnable? = null

    fun scheduleNotification(
        context: Context,
        event: Event,
        onNotificationStatusChanged: (Boolean) -> Unit
    ) {
        // ✅ Annule d'abord toute notification en attente
        cancelNotification()

        // ✅ Planifie une nouvelle notification après 10s
        toastHandler = Handler(Looper.getMainLooper())
        toastRunnable = Runnable {
            // ✅ Afficher la notification
            Toast.makeText(context, "Notification pour l'événement: ${event.title}", Toast.LENGTH_SHORT).show()

            // ✅ Mettre à jour l'état une fois la notif affichée
            onNotificationStatusChanged(false)

            // ✅ Mettre à jour SharedPreferences
            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(event.id.toString(), false).apply()
        }
        toastHandler?.postDelayed(toastRunnable!!, 10_000) // 10 secondes
    }

    fun cancelNotification() {
        // ✅ Supprime la tâche en attente pour éviter son exécution
        toastRunnable?.let { toastHandler?.removeCallbacks(it) }
    }
}



