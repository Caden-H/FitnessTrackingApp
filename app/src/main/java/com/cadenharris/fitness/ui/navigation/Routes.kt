package com.cadenharris.fitness.ui.navigation

data class Screen(val route: String)

object Routes {

    val launchNavigation = Screen("launchnavigation")
    val fitnessNavigation = Screen("fitnessnavigation")
    val editFitness = Screen("editfitness")
    val launch = Screen("launch")
    val home = Screen("home")
    val signIn = Screen("signin")
    val signUp = Screen("signup")
    val profile = Screen("profile")
    val history = Screen("history")
    val splashScreen = Screen("splashscreen")
}