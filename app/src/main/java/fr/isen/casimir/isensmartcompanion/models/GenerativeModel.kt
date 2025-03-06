package fr.isen.casimir.isensmartcompanion.models

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object GenerativeModelHelper {
    val generativeModel = GenerativeModel(
        // Use a model that's applicable for your use case (see "Implement basic use cases" below)
        modelName = "gemini-1.5-flash", // for Text to text
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = "AIzaSyAsMLILSKbGf1TvCoIzj8Mfr-R8sSDeklQ"
    )

    val scope= CoroutineScope(Dispatchers.IO)
}
