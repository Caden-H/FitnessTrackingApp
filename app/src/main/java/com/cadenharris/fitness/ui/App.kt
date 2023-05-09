package com.cadenharris.fitness.ui
import androidx.compose.runtime.Composable
import com.cadenharris.fitness.ui.navigation.RootNavigation
import com.cadenharris.fitness.ui.theme.FitnessTheme

@Composable
fun App() {
    FitnessTheme {
        RootNavigation()
    }
}
