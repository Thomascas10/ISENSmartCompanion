package fr.isen.casimir.isensmartcompanion

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.casimir.isensmartcompanion.api.NetworkManager
import fr.isen.casimir.isensmartcompanion.models.Event
import fr.isen.casimir.isensmartcompanion.screens.EventsScreen
import fr.isen.casimir.isensmartcompanion.screens.HistoryScreen
import fr.isen.casimir.isensmartcompanion.screens.MainScreen
import fr.isen.casimir.isensmartcompanion.screens.TabView
import fr.isen.casimir.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            // setting up the individual tabs
            val homeTab = TabBarItem(title = "Home", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home)
            val alertsTab = TabBarItem(title = "Events", selectedIcon = ImageVector.vectorResource(id = R.drawable.event_svgrepo_com), unselectedIcon = ImageVector.vectorResource(id = R.drawable.event_svgrepo_com))
            val settingsTab = TabBarItem(title = "History", selectedIcon = ImageVector.vectorResource(id = R.drawable.history_icon), unselectedIcon = ImageVector.vectorResource(id = R.drawable.history_icon))


            // creating a list of all the tabs
            val tabBarItems = listOf(homeTab, alertsTab, settingsTab)

            // creating our navController
            val navController = rememberNavController()


            ISENSmartCompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = { TabView(tabBarItems, navController) }) { innerPadding ->
                    NavHost(navController = navController, startDestination = homeTab.title) {
                        composable(homeTab.title) {
                            MainScreen(innerPadding)
                        }
                        composable(alertsTab.title) {
                            EventsScreen(navController)
                        }
                        composable(settingsTab.title) {
                            HistoryScreen()
                        }
                    }
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("lifecycle", "MainActivityRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecycle", "MainActivityResume")
    }

    override fun onStop() {
        Log.d("lifecycle", "MainActivityStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("lifecycle", "MainActivityDestroy")
        super.onDestroy()
    }
}


fun fetchEvents(){
    val call = NetworkManager.api.getEvents()
    call.enqueue(object: Callback<List<Event>>{
        override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
            response.body()?.forEach {
                Log.d("request", "event : ${it.title}")
            }
        }

        override fun onFailure(call: Call<List<Event>>, t: Throwable) {
            Log.e("request", t.message ?: "request fail")
        }
    })
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ISENSmartCompanionTheme {
        MainScreen(PaddingValues(8.dp))
    }
}

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Permission refusée !", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

/*@Composable
fun MainScreen() {
    var question by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("Je suis une réponse factice de l'IA.") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo et Titre
        Image(
            painter = painterResource(id = R.drawable.logo), // Assurez-vous d'avoir un logo dans res/drawable
            contentDescription = "Logo",
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "Assistant IA",
            fontSize = 32.sp,
            style = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Champ de saisie pour la question
        BasicTextField(
            value = question,
            onValueChange = { question = it },
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton d'envoi
        Button(onClick = {
            // Ici, vous pouvez ajouter la logique pour traiter la question avec l'IA
            response = "Je ne sais pas encore répondre, mais bientôt !"
        }) {
            Text(text = "Envoyer")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Réponse de l'IA
        Text(
            text = response,
            fontSize = 18.sp,
            style = TextStyle(color = Color.Gray)
        )
    }
}*/