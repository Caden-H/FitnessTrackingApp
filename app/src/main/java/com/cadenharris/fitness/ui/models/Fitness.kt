package com.cadenharris.fitness.ui.models

import java.util.*

data class Fitness(
    val id: String? = null,
    val userId: String? = null,
    val name: String? = null,
    val usage: Int? = null,
    val count: Int? = null,
    val description: String? = null,
    val date: String? = null,
    val log: MutableMap<String,Int>? = null
) {
    companion object Usage {
        const val USAGE_TRACKING = 0 // For tracking exercises
        const val USAGE_PERSONAL = 1 // For storing user data
    }
}