package com.cadenharris.fitness.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cadenharris.fitness.ui.components.Chart
import com.cadenharris.fitness.ui.components.LogEntry
import com.cadenharris.fitness.ui.models.Fitness
import com.cadenharris.fitness.ui.navigation.Routes
import com.cadenharris.fitness.ui.theme.Teal200
import com.cadenharris.fitness.ui.viewmodels.FitnessViewModel
import com.cadenharris.fitness.ui.viewmodels.RootNavigationViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class HomeScreenState {
    var today = Date(LocalDate.now().year, LocalDate.now().monthValue, LocalDate.now().dayOfMonth)

    val name by mutableStateOf("")
    val usage by mutableStateOf(Fitness.USAGE_TRACKING)
    val count by mutableStateOf("0")
    val description by mutableStateOf("")
    val date by mutableStateOf(today)
    val log by mutableStateMapOf(today to 0)
    var dropdownExpanded by mutableStateOf(false)
    var countBoxValue by mutableStateOf("")
    var lifetimeBoxValue by mutableStateOf("")
    var textFieldSize by mutableStateOf(Size.Zero)
    var selectedFitnessName by mutableStateOf("")
    var awayfromSensor by mutableStateOf(true)
    var showPrevious by mutableStateOf(false)

    var screenNumber by mutableStateOf(1)

}


@Composable
fun rememberHomeState(): HomeScreenState {
    return remember { HomeScreenState() }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val homeState = rememberHomeState()
    val viewModel: FitnessViewModel = viewModel()
    val state = viewModel.uiState
    val scope = rememberCoroutineScope()

    val fitnessList by remember() {
        derivedStateOf {
            state.fitness
        }
    }
    val viewModelRoot: RootNavigationViewModel = viewModel()

    LaunchedEffect(true) {
        val loadingTodos = async { viewModel.getFitness() }
//        delay(3000)
        loadingTodos.await()
        state.loading = false

        // reload data when coming back to screen with exercise already selected
        if (state.currentFitness.name != "Select exercise") {
            homeState.countBoxValue = state.currentFitness.log?.get(state.today).toString()
            homeState.lifetimeBoxValue = state.currentFitness.count.toString()
            homeState.selectedFitnessName = state.currentFitness.name!!
        }

    }
    
    Scaffold (

        floatingActionButton = {
            if (homeState.screenNumber == 1) {
                FloatingActionButton(onClick = {navController.navigate(Routes.editFitness.route)}) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add exercise")
                }
            } else if (homeState.screenNumber == 2) {
                FloatingActionButton(onClick = {
                    viewModelRoot.signOutUser()
                    navController.navigate(Routes.launchNavigation.route) {
                        popUpTo(0) // clear back stack and basically start the app from scratch
                    }
                }) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Sign Out")
                }
            }



        },
        bottomBar = {
            if (currentDestination?.hierarchy?.none { it.route == Routes.launchNavigation.route || it.route == Routes.splashScreen.route } == true) {

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                BottomNavigation() {

                    BottomNavigationItem(
                        label = {
                            Text(text = "History")
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.List, contentDescription = "History nav icon")
                        },
                        selected = homeState.screenNumber == 0,
                        onClick = {
                            homeState.screenNumber = 0
                        }
                    )

                    BottomNavigationItem(
                        label = {
                            Text(text = "Home")
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.Home, contentDescription = "Home nav icon")
                        },
                        selected = homeState.screenNumber == 1,
                        onClick = {
                            homeState.screenNumber = 1
                            homeState.countBoxValue = state.currentFitness.log?.get(state.today).toString() ?: "0"
                        }
                    )

                    BottomNavigationItem(
                        label = {
                            Text(text = "Profile")
                        },
                        icon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "Profile nav icon")
                        },
                        selected = homeState.screenNumber == 2,
                        onClick = {
                            homeState.screenNumber = 2
                        }
                    )

                }
            }
        },
        content = {
            Column() {

                Row {

                    Column(Modifier.padding(20.dp)) {

                        // Create an Outlined Text Field
                        // with icon and not expanded
                        OutlinedTextField(
                            value = homeState.selectedFitnessName,
                            onValueChange = { homeState.selectedFitnessName = it },

                            modifier = Modifier
                                .clickable(onClick = {
                                    homeState.dropdownExpanded = !homeState.dropdownExpanded
                                })
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    // This value is used to assign to
                                    // the DropDown the same width
                                    homeState.textFieldSize = coordinates.size.toSize()
                                },
                            enabled = false,
                            label = {Text("Tracking")},
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", Modifier.clickable{
                                    if (state.currentFitness.name != "Select exercise") {
                                        navController.navigate("editfitness?id=${state.currentFitness.id}")
                                    }
                                })

                            }
                        )

                        if(state.currentFitness.description != "") {
                            Row(horizontalArrangement = Arrangement.Center) {
                                state.currentFitness.description?.let { it1 -> Text(text = it1, modifier = Modifier.padding(4.dp), style = MaterialTheme.typography.subtitle2) }
                            }
                        }

                        DropdownMenu(
                            expanded = homeState.dropdownExpanded,
                            onDismissRequest = { homeState.dropdownExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(8.dp)
                        ) {
                            fitnessList.forEach { fitness ->
                                DropdownMenuItem(
                                    onClick = {
                                        homeState.selectedFitnessName = fitness.name.toString()
                                        state.currentFitness = fitness
                                        homeState.countBoxValue = state.currentFitness.log?.get(state.today).toString()
                                        homeState.lifetimeBoxValue = state.currentFitness.count.toString()
                                        homeState.dropdownExpanded = false
                                    }
                                ) {
                                    Text(text = fitness.name!!)
                                }

                            }

                        }


                    }

                }


               Box(modifier = Modifier.padding(8.dp)) {
                   // Home Screen
                   if(homeState.screenNumber == 1) {
                       Column {

                           fun addCount(value: Int) {
                               scope.launch {
                                   state.buttonEnabled = false
                                   println(state.currentFitness)
                                   viewModel.addFitnessCount(state.currentFitness, value)
                                   if (state.currentFitness.log?.get(state.today) == null) {
                                       homeState.countBoxValue = "0"
                                   } else {
                                       homeState.countBoxValue = state.currentFitness.log?.get(state.today).toString()
                                   }
                                   homeState.lifetimeBoxValue = state.currentFitness.count.toString()
                               }
                           }

                           @Composable
                           fun AddButton(value: Int) {
                               Button(
                                   modifier = Modifier
                                       .padding(4.dp)
                                       .height(69.dp)
                                       .width(69.dp),
                                   shape = RoundedCornerShape(40.dp),
                                   onClick = {
                                        addCount(value)
                                   }, enabled = state.buttonEnabled, colors = ButtonDefaults.buttonColors(backgroundColor = Teal200) ) {
                                   if(value>0) {
                                       Text(text = "+".plus(value.toString()))
                                   } else {
                                       Text(text = "".plus(value.toString()))
                                   }

                               }
                           }

                           if(state.currentFitness.name != "Select exercise") {



                               Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                   LazyRow() {
                                       items(items = listOf(1,5,10,20,100,-1,-5,500,900), itemContent = { item ->
                                           AddButton(value = item)
                                       })
                                   }



                                   Text(text = "Today's count of ".plus(state.currentFitness.name).plus(":"), style = MaterialTheme.typography.h5, color = Color.LightGray, modifier = Modifier.padding(top = 16.dp))

                                   // Today's count display
                                   TextField(
                                       keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                       value = homeState.countBoxValue,
                                       modifier = Modifier.background(Color.Transparent),
                                       colors = TextFieldDefaults.textFieldColors(
                                           backgroundColor = Color.Transparent,
                                           unfocusedIndicatorColor = Color.Transparent,
                                           cursorColor = Color.Cyan,
                                           focusedIndicatorColor = Color.Transparent,
                                           placeholderColor = Color.Transparent
                                       ),
                                       textStyle = LocalTextStyle.current.copy(
                                           textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                                       ).merge(MaterialTheme.typography.h1),


                                       onValueChange = {
                                           if (state.buttonEnabled && it.isDigitsOnly()) {
                                               scope.launch {
                                                   state.buttonEnabled = false
                                                   println(state.currentFitness)
                                                   if (it != "" && it.toInt() > 0) {
                                                       viewModel.setFitnessCount(state.currentFitness, it.toInt())
                                                   } else {
                                                       viewModel.setFitnessCount(state.currentFitness, 0)
                                                   }

                                                   if (state.currentFitness.log?.get(state.today) == null) {
                                                       homeState.countBoxValue = "0"
                                                   } else {
                                                       homeState.countBoxValue = state.currentFitness.log?.get(state.today).toString()
                                                   }

                                                   homeState.lifetimeBoxValue = state.currentFitness.count.toString()
                                               }
                                           }

                                   })


//                                   Text(text = "Lifetime: ".plus(homeState.lifetimeBoxValue), color = Color.LightGray)
                               }

                           } else {
                               Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                                   Text(text = "Select or create an Exercise to continue!")
                               }
                           }



                           val ctx = LocalContext.current
                           val sensorManager: SensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
                           val proximitySensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)


                           val proximitySensorEventListener = object : SensorEventListener {
                               override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                                   // method to check accuracy changed in sensor.
                               }

                               // on below line we are creating a sensor on sensor changed
                               override fun onSensorChanged(event: SensorEvent) {
                                   // check if the sensor type is proximity sensor.
                                   if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                                       // on below line we are checking if the
                                       // object is near or away from the sensor.
                                       if (event.values[0] == 0f) {
                                           if (homeState.awayfromSensor) {
                                               homeState.awayfromSensor = false
                                               println("Hovered")
                                               if (state.buttonEnabled) {
                                                   addCount(1)
                                               }
                                           }
                                       } else {
                                           homeState.awayfromSensor = true
                                       }
                                   }
                               }
                           }
                           sensorManager.registerListener( proximitySensorEventListener,proximitySensor, SensorManager.SENSOR_DELAY_FASTEST)

                           
                       }
                   }

                   // History Screen
                   if(homeState.screenNumber == 0) {

                       if(state.currentFitness.name != "Select exercise") {


                           Column(modifier=Modifier.fillMaxSize()) {


                               Column(modifier=Modifier.fillMaxHeight(.60f)) {
                                   if(state.currentFitness.usage == Fitness.USAGE_TRACKING) {
                                       var totalLife = 0
                                       var totalWeek = 0
                                       var dayCount = 1
                                       for (pair in state.currentFitness.log!!.toList()) {
                                           if (dayCount <= 7) {
                                               totalWeek += pair.second
                                               dayCount++
                                           }
                                           totalLife += pair.second
                                       }

                                       Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                                           Text(text = "Lifetime: ".plus(totalLife), style = MaterialTheme.typography.subtitle2, fontWeight = FontWeight.Light)
                                           Text(text = " · Past Week: ".plus(totalWeek), style = MaterialTheme.typography.subtitle2, fontWeight = FontWeight.Light)
                                       }
                                   } else { // Display weekly progress if it is a stat
                                       var weekChange = 0
                                       if (state.currentFitness.log!!.toList().size >= 7)
                                           weekChange = state.currentFitness.log!!.toList().get(6).second - state.currentFitness.log!!.toList().get(0).second

                                       Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                                           Text(text = "Weekly change: ".plus(weekChange.toString()), style = MaterialTheme.typography.subtitle2, fontWeight = FontWeight.Light)
                                       }
                                   }



                                   LazyColumn(modifier = Modifier
                                       .fillMaxHeight(.95f)
                                       .padding(16.dp)) {
                                       items(state.currentFitness.log!!.toList()) { day ->
                                           LogEntry(
                                               fitness = state.currentFitness,
                                               logValue = day,
                                               today = state.today,
                                               onDeletePressed = {
                                                   scope.launch {
                                                       viewModel.removeDay(state.currentFitness, day.toList()[0].toString())
                                                       if (state.currentFitness.log?.get(state.today) == null) {
                                                           homeState.countBoxValue = "0"
                                                       } else {
                                                           homeState.countBoxValue = state.currentFitness.log?.get(state.today).toString()
                                                       }
                                                   }
                                                   homeState.selectedFitnessName = state.currentFitness.name.toString()
                                                   homeState.countBoxValue = state.currentFitness.log?.get(state.today).toString()
                                                   homeState.lifetimeBoxValue = state.currentFitness.count.toString()
                                                   homeState.dropdownExpanded = false

                                               }
                                           )
                                           Spacer(modifier = Modifier.height(8.dp))
                                       }
                                   }
                               }

                               // Show the chart/graph
                               Column(verticalArrangement = Arrangement.Top, modifier= Modifier.fillMaxHeight(.84f)) {
                                   var numberOfGraphedDays = 7
                                   if (state.currentFitness.log!!.toList().size < 7) {
                                       numberOfGraphedDays = state.currentFitness.log!!.toList().size
                                   }
                                   var dayList = mutableListOf<String>()
                                   var valueList = mutableListOf<Float>()

                                   state.currentFitness.log!!.toList().forEach { entry ->
                                       if (numberOfGraphedDays > 0) {
                                           dayList.add(entry.first.toString().split("/")[1].plus("/").plus(entry.first.toString().split("/")[2]))
                                           valueList.add(entry.second.toFloat())
                                           numberOfGraphedDays--
                                       }
                                   }
                                   val maxChart = valueList.max().toInt()
                                   dayList.reverse()
                                   valueList.reverse()

                                   valueList.forEach{ item ->
                                       valueList[valueList.indexOf(item)] = valueList[valueList.indexOf(item)] / maxChart
                                   }

                                   Chart(
                                       barValue = valueList,
                                       xAxisScale = dayList,
                                       total_amount = maxChart
                                   )
                               }





                           }

                       } else {
                           Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                               Text(text = "Select an Exercise to view your log.")
                           }
                       }



                   }



                   // Profile Screen
                   if(homeState.screenNumber == 2) {

                       Column {
                           //Top
                           Column(verticalArrangement = Arrangement.Top) {
                               Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 16.dp)) {
                                   Text(text = "Your stats:", style = MaterialTheme.typography.subtitle2)
                                   Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                                       Text(text = "Show Previous", style = MaterialTheme.typography.subtitle2)
                                       Checkbox(checked = homeState.showPrevious, onCheckedChange = {homeState.showPrevious = !homeState.showPrevious})
                                   }
                               }

                               LazyColumn(modifier = Modifier
                                   .fillMaxHeight(.95f)
                                   .padding(16.dp)) {
                                   items(fitnessList) { fitness ->
                                       if (fitness.usage == Fitness.USAGE_PERSONAL) {
                                           Surface(
                                               elevation = 2.dp,
                                               shape = RoundedCornerShape(4.dp),
                                           ) {

                                               Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                                   Text(text = fitness.name!!, modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold)

                                                   var previousValue = 0
                                                   var previousDay = ""
                                                   if (fitness.log!!.toList().size > 1 && homeState.showPrevious) {
                                                       previousValue = fitness.log!!.toList()[1].second
                                                       previousDay = fitness.log!!.toList()[1].first
                                                       Text(text = "Previous:", modifier = Modifier.padding(4.dp), style = MaterialTheme.typography.caption)
                                                       Text(text = fitness.log?.get(previousDay).toString(), style = MaterialTheme.typography.caption, modifier = Modifier.padding(end = 4.dp))
                                                   }


                                                   Row( horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {


                                                       Text(text = "Today:", modifier = Modifier.padding(4.dp))
                                                       TextField(
                                                           modifier = Modifier
                                                               .background(Color.Transparent)
                                                               .padding(8.dp)
                                                               .width(90.dp),
                                                           shape = RoundedCornerShape(8.dp),
                                                           colors = TextFieldDefaults.textFieldColors(
//                                                           backgroundColor = Color.Transparent,
                                                               unfocusedIndicatorColor = Color.Transparent,
                                                               cursorColor = Color.Cyan,
                                                               focusedIndicatorColor = Color.Transparent,
                                                               placeholderColor = Color.Transparent
                                                           ),
                                                           value = fitness.log?.get(state.today).toString(),
                                                           onValueChange = {
                                                               if (it.isDigitsOnly() && state.buttonEnabled && it != "") {
                                                                   scope.launch {
                                                                       viewModel.setFitnessCount(fitness, it.toInt())
                                                                   }
                                                               }
                                                           },
                                                       )
                                                   }

                                               }

                                           }

                                           Spacer(modifier = Modifier.height(8.dp))

                                       }

                                   }
                               }
                           }


                           //Bottom
                           Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
                               Button(onClick = {
                                   viewModelRoot.signOutUser()
                                   navController.navigate(Routes.launchNavigation.route) {
                                       popUpTo(0) // clear back stack and basically start the app from scratch
                                   }
                               }, colors = ButtonDefaults.buttonColors(backgroundColor = Teal200)
                               ) {
                                   Text(text = "Sign out")
                               }
                               Text(text = "TEST")
                               Spacer(modifier = Modifier.height(164.dp))
                           }

                       }

                       
                   }
               }

            }
        }
    )
}

