package fr.isen.casimir.isensmartcompanion.screens

import fr.isen.casimir.isensmartcompanion.models.Event

object FakeEventData {
    val eventList = listOf(
        Event(
            id = "0d1d122c",
            title = "Soirée BDE",
            description = "Un moment pour accueillir les nouveaux élèves et renforcer la cohésion entre les promotions avec des activités autour de la santé, l'écologie, et la vie associative.",
            date = "24 septembre 2024",
            location = "Plage du Mourillon",
            category = "Vie associative"
        ),
        Event(
            id = "1e2d345a",
            title = "Gala annuel de l'ISEN",
            description = "Soirée prestigieuse organisée par le BDE pour célébrer les réussites de l'année dans une ambiance festive.",
            date = "10 décembre 2024",
            location = "Palais Neptune, Toulon",
            category = "BDE"
        ),
        Event(
            id = "2f3g456b",
            title = "Tournoi de futsal ISEN",
            description = "Compétition sportive organisée par le BDS pour les amateurs de football en salle.",
            date = "15 octobre 2024",
            location = "Complexe sportif de Toulon",
            category = "BDS"
        )
    )
}
