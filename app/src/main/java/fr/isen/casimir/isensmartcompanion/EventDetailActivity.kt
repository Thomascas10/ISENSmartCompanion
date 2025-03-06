package fr.isen.casimir.isensmartcompanion

/*import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.casimir.isensmartcompanion.models.Event
import fr.isen.casimir.isensmartcompanion.ui.theme.ISENSmartCompanionTheme

class EventDetailActivity : ComponentActivity() {

    private lateinit var event: Event
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        val eventTitle: TextView = findViewById(R.id.eventTitle)
        val eventDate: TextView = findViewById(R.id.eventDate)
        val eventLocation: TextView = findViewById(R.id.eventLocation)

        eventTitle.text = event.title
        eventDate.text = event.date
        eventLocation.text = event.location


        val event = intent.getSerializableExtra("event") as? Event
        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        this.event = intent.getParcelableExtra("event") ?: throw IllegalStateException("Event not found")
        if (event != null) {
            findViewById<TextView>(R.id.eventTitle).text = event.title
        }


        val notifyButton: Button = findViewById(R.id.notifyButton)
        val isNotified = sharedPreferences.getBoolean(event?.id.toString(), false)
        updateButtonText(notifyButton, isNotified)

        notifyButton.setOnClickListener {
            val newState = !sharedPreferences.getBoolean(event?.id.toString(), false)
            if (event != null) {
                sharedPreferences.edit().putBoolean(event.id.toString(), newState).apply()
            }

            if (newState) {
                if (event != null) {
                    scheduleNotification(this, event)
                }
                Toast.makeText(this, "Notification programmée dans 10s", Toast.LENGTH_SHORT).show()
            } else {
                if (event != null) {
                    cancelNotification(this, event)
                }
                Toast.makeText(this, "Notification annulée", Toast.LENGTH_SHORT).show()
            }

            updateButtonText(notifyButton, newState)
        }
        setContent {
                event?.let {
                    EventDetailScreen(event)
            }
        }
    }
    private fun updateButtonText(button: Button, isNotified: Boolean) {
        button.text = if (isNotified) "Désactiver la notification" else "Activer la notification"
    }
}

@Composable
fun EventDetailScreen(event: Event) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = event.title, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Date: ${event.date}")
        Text(text = "Lieu: ${event.location}")
        Text(text = "Catégorie: ${event.category}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = event.description)
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    ISENSmartCompanionTheme {
        Greeting("Android")
    }
}*/

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.casimir.isensmartcompanion.models.Event
import fr.isen.casimir.isensmartcompanion.models.NotificationViewModel

class EventDetailActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val notificationViewModel: NotificationViewModel by viewModels() // ✅ Déclaration du ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crée le canal de notification dès le lancement de l'application
        createNotificationChannel(this)

        // ✅ Récupérer l'événement depuis Intent (en Serializable)
        val event = intent.getSerializableExtra("event") as? Event
            ?: throw IllegalStateException("Event not found")

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        setContent {
            EventDetailScreen(event, sharedPreferences, notificationViewModel)
        }
    }
}

@Composable
fun EventDetailScreen(event: Event, sharedPreferences: SharedPreferences, viewModel: NotificationViewModel) {
    var isNotified by remember { mutableStateOf(sharedPreferences.getBoolean(event.id.toString(), false)) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = event.title, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Date: ${event.date}")
        Text(text = "Lieu: ${event.location}")
        Text(text = "Catégorie: ${event.category}")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = event.description)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val newState = !isNotified

            // ✅ Mise à jour immédiate du bouton
            isNotified = newState
            sharedPreferences.edit().putBoolean(event.id.toString(), newState).apply()

            if (newState) {
                // ✅ Planifier la notification après 10s
                viewModel.scheduleNotification(context, event) { updatedState ->
                    isNotified = updatedState
                    sharedPreferences.edit().putBoolean(event.id.toString(), updatedState).apply()
                }
                Toast.makeText(context, "Notification programmée dans 10s", Toast.LENGTH_SHORT).show()
            } else {
                // ✅ Annuler immédiatement la notification
                viewModel.cancelNotification()
                Toast.makeText(context, "Notification annulée", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(if (isNotified) "Désactiver la notification" else "Activer la notification")
        }
    }
}
