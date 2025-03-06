package fr.isen.casimir.isensmartcompanion.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import fr.isen.casimir.isensmartcompanion.EventDetailActivity
import fr.isen.casimir.isensmartcompanion.api.NetworkManager
import fr.isen.casimir.isensmartcompanion.models.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun EventsScreen(navController: NavHostController) {
    val events = remember { mutableStateOf<List<Event>>(listOf()) }
    val sharedPreferences = navController.context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)



    LaunchedEffect(Unit) {
        val call = NetworkManager.api.getEvents()
        call.enqueue(object: Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                events.value = response.body() ?: listOf()
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Log.e("request", t.message ?: "request fail")
            }
        })
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp, start = 16.dp, end = 16.dp, bottom = 80.dp)
    ) {
        items(events.value) { event ->
            val isNotified = sharedPreferences.getBoolean(event.id.toString(), false)

            EventItem(event = event, isNotified = isNotified , onClick = {
                val context = navController.context
                val intent = Intent(context, EventDetailActivity::class.java)
                intent.putExtra("event", event)
                context.startActivity(intent)
            })
        }
    }
}

@Composable
fun EventItem(event: Event, isNotified: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isNotified) Color(0xFFFFE082) else Color.LightGray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = event.title, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = event.date)
            Text(text = event.location)
        }
    }
}

