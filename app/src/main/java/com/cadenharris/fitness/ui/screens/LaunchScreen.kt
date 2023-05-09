package com.cadenharris.fitness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cadenharris.fitness.ui.navigation.Routes
import com.cadenharris.fitness.ui.theme.Purple200
import com.cadenharris.fitness.ui.theme.Purple500
import com.cadenharris.fitness.ui.theme.Teal200


@Composable
fun LaunchScreen(navHostController: NavHostController) {
    LaunchedEffect(true) {

    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.h2,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Get ready to track your fitness!",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button( onClick = { navHostController.navigate(Routes.signUp.route) }, colors = ButtonDefaults.buttonColors(backgroundColor = Teal200) ) {
                    Text(text = "Create Account")
                }
                TextButton(onClick = { navHostController.navigate(Routes.signIn.route) }, colors = ButtonDefaults.textButtonColors(contentColor = Teal200)) {
                    Text(text = "Sign in")
                }
            }
        }
    }
}