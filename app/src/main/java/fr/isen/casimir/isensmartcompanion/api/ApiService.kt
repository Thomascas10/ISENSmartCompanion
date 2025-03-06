package fr.isen.casimir.isensmartcompanion.api

import retrofit2.Call
import fr.isen.casimir.isensmartcompanion.models.Event
import retrofit2.http.GET

interface ApiService {
    @GET("events.json")
    fun getEvents(): Call<List<Event>>
}