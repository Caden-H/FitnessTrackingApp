package com.cadenharris.fitness.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.cadenharris.fitness.ui.components.FormField
import com.cadenharris.fitness.ui.navigation.Routes
import com.cadenharris.fitness.ui.theme.Teal200
import com.cadenharris.fitness.ui.viewmodels.SignInViewModel
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(navController: NavController) {
    val viewModel: SignInViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val state = viewModel.uiState

    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) {
            navController.navigate(Routes.fitnessNavigation.route) {
                popUpTo(0)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround) {
        Surface(elevation = 2.dp) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Text(modifier = Modifier.padding(8.dp), text = "Sign In", style = MaterialTheme.typography.h5)
                FormField(
                    value = state.email,
                    onValueChange = { state.email = it },
                    placeholder = { Text("Email") },
                    error = state.emailError,

                )
                FormField(
                    value = state.password,
                    onValueChange = { state.password = it },
                    placeholder = { Text("Password") },
                    error = state.passwordError,
                    password = true
                )
                Row (
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ){
                    TextButton(onClick = { navController.popBackStack() }, colors = ButtonDefaults.textButtonColors(contentColor = Teal200)) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = { scope.launch { viewModel.signIn() } }, elevation = null, colors = ButtonDefaults.buttonColors(backgroundColor = Teal200) ) {
                        Text(text = "Sign in")
                    }
                }
                Text(
                    text = state.errorMessage,
                    style = TextStyle(color = MaterialTheme.colors.error),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}