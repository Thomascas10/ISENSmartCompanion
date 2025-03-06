package fr.isen.casimir.isensmartcompanion.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fr.isen.casimir.isensmartcompanion.HistoryManager
import fr.isen.casimir.isensmartcompanion.R
import fr.isen.casimir.isensmartcompanion.models.GenerativeModelHelper
import fr.isen.casimir.isensmartcompanion.models.GenerativeModelHelper.scope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(innerPadding: PaddingValues) {
    val context = LocalContext.current
    var userInput = remember { mutableStateOf("") }
    var chatList = remember{ mutableStateListOf("")}
    Column(
        modifier = Modifier.fillMaxWidth()
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painterResource(R.drawable.isen2),
            context.getString(R.string.isen_logo))
        Text(context.getString(R.string.isen_logo))
        Text("", modifier = Modifier
            .fillMaxSize()
            .weight(0.5F))
        LazyColumn {
            items(chatList) { eachChat ->
                Text("$eachChat")
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .padding(8.dp)) {
            TextField(
                value = userInput.value,
                onValueChange = { newValue ->
                    userInput.value = newValue
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent),
                modifier = Modifier.weight(1F))
            OutlinedButton ( onClick = {
                Toast.makeText(context, "user input : ${userInput.value}", Toast.LENGTH_LONG).show()
                scope.launch{
                    try{
                        val response = GenerativeModelHelper.generativeModel.generateContent((userInput.value))
                        val aiResponse = response.text.toString()

                        // Ajoute la réponse de l'IA à la liste des chats
                        chatList.add("Toi : ${userInput.value}")
                        chatList.add("IA : $aiResponse")

                        // Sauvegarde dans l'historique
                        val historyManager = HistoryManager(context)
                        historyManager.addInteraction(userInput.value, aiResponse)

                        // Efface le champ de texte après l'envoi
                        userInput.value = ""

                    } catch (e: Exception) {
                        Log.e( "gemini", "Error : ${e.message}")
                    }
                }
            },  modifier = Modifier.background(Color.Red, shape = RoundedCornerShape(50)),
                content = {
                    Image(painterResource(R.drawable.send), "")
                })
        }

    }
}

