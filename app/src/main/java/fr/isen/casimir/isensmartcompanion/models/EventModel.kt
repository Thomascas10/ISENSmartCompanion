package fr.isen.casimir.isensmartcompanion.models

import java.io.Serializable

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) : Serializable

/*class EventModel {
    companion object {
        fun fakeEvents(): List<Event> {
            return listof(
                Event(
                    id = 1,
                    title = "Soirée BDE",
                    description = "Une soirée inoubliable organisée par le BDE.",
                    date = "2025-03-10",
                    location = "Salle des fêtes",
                    category = "Festif"
                )
            )
        }
    }
}*/