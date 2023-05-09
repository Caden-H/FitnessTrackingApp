package com.cadenharris.fitness.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cadenharris.fitness.ui.components.FormField
import com.cadenharris.fitness.ui.theme.Teal200
import com.cadenharris.fitness.ui.viewmodels.FitnessModificationViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.launch


@Composable
fun FitnessModificationScreen(navHostController: NavHostController, id: String?) {
    val viewModel: FitnessModificationViewModel = viewModel()
    val state = viewModel.uiState
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.setupInitialState(id)
    }

    LaunchedEffect(state.saveSuccess) {
        println(id)
        if (state.saveSuccess) {
            println("SAVE SUCCESS")
            Toast.makeText(
                context,
                "Exercise saved successfully",
                Toast.LENGTH_SHORT
            ).show()
            navHostController.popBackStack()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {




        FormField(
            value = state.name,
            onValueChange = { state.name = it },
            placeholder = { Text(text = "Name") }
        )
        Spacer(modifier = Modifier.height(4.dp))
        FormField(
            value = state.description,
            onValueChange = { state.description = it },
            placeholder = { Text(text = "Description") }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = viewModel.uiState.boxChecked, onCheckedChange = {viewModel.uiState.boxChecked = !viewModel.uiState.boxChecked})
            Text(text = "Statistic")

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {

                if (id != "new") {
                    TextButton(onClick = {
                        navHostController.popBackStack()
                        scope.launch {
                            viewModel.deleteFitness(id!!)
                        }
                    },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)) {
                        Text(text = "Delete")
                    }
                }

                TextButton(onClick = { navHostController.popBackStack() }, colors = ButtonDefaults.textButtonColors(contentColor = Teal200)) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(colors = ButtonDefaults.buttonColors(backgroundColor = Teal200), onClick = {
                    scope.launch {
                        viewModel.saveFitness()
                    }
                }) {
                    Text(text = "Save")
                }
            }
        }


        Text(
            text = state.errorMessage,
            style = TextStyle(color = MaterialTheme.colors.error),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Right
        )

        Column (verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight()) {
            // ca-app-pub-2639351679368165/8683320166
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdSize.BANNER)
                        adUnitId = "ca-app-pub-3940256099942544/6300978111"
                        loadAd(AdRequest.Builder().build())
                    }
                }
            )
        }


    }
}