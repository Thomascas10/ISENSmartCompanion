package fr.isen.casimir.isensmartcompanion

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatEntry(
    val question: String,
    val answer: String,
    val date: String
)

val Context.dataStore by preferencesDataStore("user_history")

class HistoryManager(private val context: Context) {
    private val HISTORY_KEY = stringSetPreferencesKey("history")
    private val sharedPreferences = context.getSharedPreferences("history_prefs", Context.MODE_PRIVATE)

    private val _historyFlow = MutableStateFlow<List<ChatEntry>>(loadHistory())
    val historyFlow = _historyFlow.asStateFlow()

    // 🔹 Récupérer l'historique
    /*val historyFlow: Flow<List<String>> = context.dataStore.data
        .map { preferences ->
            preferences[HISTORY_KEY]?.toList() ?: emptyList()
        }*/

    // 🔹 Ajouter une interaction
    /*suspend fun addInteraction(question: String, answer: String) {
        val newEntry = "$question → $answer"
        context.dataStore.edit { preferences ->
            val currentHistory = preferences[HISTORY_KEY]?.toMutableSet() ?: mutableSetOf()
            currentHistory.add(newEntry)
            preferences[HISTORY_KEY] = currentHistory
        }
    }*/

    // 🔹 Supprimer l'historique
    /*suspend fun clearHistory() {
        context.dataStore.edit { it.remove(HISTORY_KEY) }
    }*/

    fun addInteraction(userInput: String, aiResponse: String) {
        val currentDate = getCurrentDate() // Obtenir la date actuelle
        val newEntry = ChatEntry(userInput, aiResponse, currentDate)
        val updatedHistory = _historyFlow.value + newEntry
        _historyFlow.value = updatedHistory
        saveHistory(updatedHistory)
    }

    fun deleteInteraction(entry: ChatEntry) {
        val updatedHistory = _historyFlow.value.filter { it != entry } // Supprime l'entrée sélectionnée
        _historyFlow.value = updatedHistory
        saveHistory(updatedHistory)
    }

    fun clearHistory() {
        _historyFlow.value = emptyList()
        sharedPreferences.edit().clear().apply()
    }

    private fun saveHistory(history: List<ChatEntry>) {
        val historySet = history.map { "${it.question}||${it.answer}||${it.date}" }.toSet()
        sharedPreferences.edit().putStringSet("history", historySet).apply()
    }

    private fun loadHistory(): List<ChatEntry> {
        return sharedPreferences.getStringSet("history", emptySet())?.map {
            val parts = it.split("||")
            ChatEntry(parts[0], parts[1], parts[2])
        } ?: emptyList()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }
}