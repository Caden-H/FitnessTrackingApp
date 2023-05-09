package com.cadenharris.fitness.ui.components


import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.cadenharris.fitness.ui.models.Fitness
import kotlinx.coroutines.launch

enum class SwipeState {
    OPEN,
    CLOSED
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LogEntry(
    fitness: Fitness,
    today: String,
    logValue: Pair<String,Int>,
    onDeletePressed: () -> Unit = {}
) {
    var dayValue by remember {mutableStateOf("")}
    var swipeableState = rememberSwipeableState(initialValue = SwipeState.CLOSED)
    dayValue = logValue.toList()[1].toString()
    val anchors = mapOf(
        0f to SwipeState.CLOSED,
        -200f to SwipeState.OPEN
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
            )
    ) {
        Row(modifier = Modifier
            .height(IntrinsicSize.Max)
            .fillMaxWidth()
            .fillMaxHeight(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    onDeletePressed()
                    dayValue = logValue.toList()[1].toString()
                    swipeableState.performDrag(200f)
                          },
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(.5f)
                ,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
        Surface(
            modifier = Modifier
                .height(40.dp)
                .align(Alignment.Center)
                .offset { IntOffset(swipeableState.offset.value.toInt(), 0) },
            elevation = 2.dp,
            shape = RoundedCornerShape(4.dp),
        ) {
            Column (){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight()) {
                        Text(text = dayValue ?: "", style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
                        Text(text = " · ", style = MaterialTheme.typography.subtitle1, modifier = Modifier.padding(start = 4.dp))
                        if (logValue.toList()[0].toString() == today) {
                            Text(text = "Today" ?: "", style = MaterialTheme.typography.subtitle2, modifier = Modifier.padding(start = 4.dp), fontWeight = FontWeight.Thin)
                        } else {
                            Text(text = logValue.toList()[0].toString().split("/")[1].plus("/").plus(logValue.toList()[0].toString().split("/")[2]) ?: "", style = MaterialTheme.typography.subtitle2, modifier = Modifier.padding(start = 4.dp), fontWeight = FontWeight.Thin)
                        }
//                        Text(text = "/".plus(logValue.toList()[0].toString().split("/")[0]) ?: "", style = MaterialTheme.typography.subtitle2, fontWeight = FontWeight.Thin) // Show year
                    }
                }
            }
        }
    }
}

