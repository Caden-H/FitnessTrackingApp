package com.cadenharris.fitness.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cadenharris.fitness.ui.theme.Purple500

@Composable
fun Chart(
    barValue: MutableList<Float>,
    xAxisScale: MutableList<String>,
    total_amount: Int
) {
    val context = LocalContext.current
    // BarGraph Dimensions
    val barGraphHeight by remember { mutableStateOf(200.dp) }
    val barGraphWidth by remember { mutableStateOf(20.dp) }
    // Scale Dimensions
    val scaleYAxisWidth by remember { mutableStateOf(50.dp) }
    val scaleLineWidth by remember { mutableStateOf(2.dp) }

    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barGraphHeight),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            // scale Y-Axis
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scaleYAxisWidth),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = total_amount.toString())
                    Spacer(modifier = Modifier.fillMaxHeight())
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = (total_amount / 2).toString())
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                }

            }

            // Y-Axis Line
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scaleLineWidth)
                    .background(Color.LightGray)
            )

            // graph
            barValue.forEach {
                Box(
                    modifier = Modifier
                        .padding(start = barGraphWidth, bottom = 5.dp)
                        .clip(CircleShape)
                        .width(barGraphWidth)
                        .fillMaxHeight(it)
                        .background(Color.Cyan)
                        .clickable {
                            Toast
                                .makeText(context, it.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                )
            }

        }

        // X-Axis Line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(scaleLineWidth)
                .background(Color.LightGray)
        )

        // Scale X-Axis
        Row(
            modifier = Modifier
                .padding(start = scaleYAxisWidth + barGraphWidth + scaleLineWidth)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(barGraphWidth)
        ) {

            xAxisScale.forEach {
                Text(
                    modifier = Modifier.width(barGraphWidth),
                    text = it,
                    textAlign = TextAlign.Center,
                    fontSize = 6.sp
                )
            }

        }

    }

}