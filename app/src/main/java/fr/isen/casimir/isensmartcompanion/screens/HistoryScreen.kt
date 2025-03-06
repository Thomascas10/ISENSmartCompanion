package fr.isen.casimir.isensmartcompanion.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.casimir.isensmartcompanion.ChatEntry
import fr.isen.casimir.isensmartcompanion.HistoryManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen() {
    /*Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Historique des activités")
    }*/

    /*val context = LocalContext.current
    val historyManager = remember { HistoryManager(context) }
    val history by historyManager.historyFlow.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Historique", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        history.forEach { entry ->
            Text(entry, modifier = Modifier.padding(8.dp))
        }

        Button(onClick = { CoroutineScope(Dispatchers.IO).launch { historyManager.clearHistory() } }) {
            Text("Effacer l'historique")
        }
    }*/


    val context = LocalContext.current
    val historyManager = remember { HistoryManager(context) }
    val history by historyManager.historyFlow.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    Box( // 📌 Utilisation de Box pour organiser les éléments
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text("Historique", fontSize = 20.sp, fontWeight = FontWeight.Bold)


            Button(
                onClick = {
                    if (history.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Rien à supprimer, aucun HistoryItems",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        coroutineScope.launch { historyManager.clearHistory() }
                    }
                },
                modifier = Modifier
                    /*.align(Alignment.BottomCenter)*/ // ✅ Positionné tout en bas
                    .fillMaxWidth()
                    .padding(top = 32.dp) // ✅ Ajoute un padding pour éviter qu'il soit collé à la navbar
            ) {
                Text("Effacer tout l'historique")
            }

            // ✅ LazyColumn prend tout l’espace disponible
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // ✅ Permet à la liste de scroller sans bloquer le bouton
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 80.dp) // ✅ Espace pour éviter recouvrement par la navbar
            ) {
                items(history) { entry ->
                    HistoryItem(entry) { selectedEntry ->
                        coroutineScope.launch {
                            historyManager.deleteInteraction(selectedEntry)
                        }
                    }
                }
            }
        }
    }

    // ✅ Bouton fixé en bas

}


@Composable
fun HistoryItem(entry: ChatEntry, onDelete: (ChatEntry) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📅 ${entry.date}", fontSize = 14.sp, fontWeight = FontWeight.Light)
            Spacer(modifier = Modifier.height(4.dp))
            Text("🙋‍♂️ Toi : ${entry.question}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("🤖 IA : ${entry.answer}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Bouton de suppression
            Button(onClick = { onDelete(entry) }) {
                Text("🗑 Supprimer")
            }
        }
    }
}
