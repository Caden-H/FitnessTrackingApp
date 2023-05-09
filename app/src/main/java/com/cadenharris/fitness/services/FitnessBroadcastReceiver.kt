package com.cadenharris.fitness.services
//
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.cadenharris.fitness.MainActivity
//import com.cadenharris.fitness.R
//import com.cadenharris.fitness.ui.models.Fitness
//import com.cadenharris.fitness.ui.repositories.FitnessRepository
//import com.cadenharris.fitness.ui.repositories.UserRepository
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import java.lang.StringBuilder
//
//class FitnessBroadcastReceiver: BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        if (context == null) return
//        if (!UserRepository.isUserLoggedIn()) return
//        GlobalScope.launch {
//            val exercisesFitness = FitnessRepository.getFitness().filter{
//                it.usage == Fitness.USAGE_TRACKING
//            }
//            if (exercisesFitness.isNotEmpty()) {
//                // display notification
//                val stringBuilder = StringBuilder()
//                exercisesFitness.forEach {
//                    stringBuilder.append("${it.name}\n")
//                }
//                val intent = Intent(context, MainActivity::class.java)
//                val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//                val builder = NotificationCompat
//                    .Builder(context, "todos")
//                    .setSmallIcon(R.drawable.ic_baseline_check_circle_24)
//                    .setContentTitle("You have incomplete high-priority tasks")
//                    .setContentText(stringBuilder.toString())
//                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
//                    .setContentIntent(pendingIntent)
//                NotificationManagerCompat.from(context).notify(
//                    0,
//                    builder.build()
//                )
//            }
//        }
//
//    }
//}