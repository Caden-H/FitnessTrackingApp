package com.cadenharris.fitness.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.cadenharris.fitness.ui.models.Fitness
import com.cadenharris.fitness.ui.repositories.FitnessRepository
import java.time.LocalDate

class FitnessScreenState {
    val _fitness = mutableStateListOf<Fitness>()
    val fitness: List<Fitness> get() = _fitness
//    var showHigherPriorityItemsFirst by mutableStateOf(false)
    var loading by mutableStateOf(true)
    val today = LocalDate.now().year.toString().plus("/").plus(LocalDate.now().monthValue.toString()).plus("/").plus(LocalDate.now().dayOfMonth.toString())
    var currentFitness:Fitness by mutableStateOf(Fitness(
        name = "Select exercise"
    ))
    var buttonEnabled by mutableStateOf(true)
}

class FitnessViewModel(application: Application): AndroidViewModel(application) {
    val uiState = FitnessScreenState()
    suspend fun getFitness() {
        val fitness = FitnessRepository.getFitness()
        uiState._fitness.clear()
        uiState._fitness.addAll(fitness)
    }

    suspend fun getDayCount(fitness: Fitness, day: String): Int {
        val amount = fitness.log?.get(day)
        return if (amount != null) {
            amount!!
        } else {
            0
        }
    }

    suspend fun removeDay(fitness: Fitness, day: String) {

        val newLog = fitness.log
        val amountToRemove = newLog?.get(day)
        if (newLog?.contains(day) == true) {
            newLog[day] = 0
        } else {
            newLog?.put(day, 0)
        }


        val fitnessCopy = fitness.copy(log = (newLog), count = (fitness.count!! - amountToRemove!!))
        // optimistically update the ui
        var index = uiState._fitness.indexOf(fitness)

        uiState._fitness[index] = fitnessCopy

        // go to Firebase
        FitnessRepository.updateFitness(fitnessCopy)
        uiState.currentFitness = uiState._fitness[index] // Update the selected exercise to the new copy of it
        uiState.buttonEnabled = true
    }

    suspend fun setFitnessCount(fitness: Fitness, amount: Int) {
        val today = LocalDate.now().year.toString().plus("/").plus(LocalDate.now().monthValue.toString()).plus("/").plus(LocalDate.now().dayOfMonth.toString())

        var differ = 0
        var newCount = 0

        val newLog = fitness.log
        if (newLog?.contains(today) == true) {
            val startCount = newLog[today]
            val differ = amount - startCount!!
            newLog[today] = amount
            newCount = fitness.count!! + differ
        } else {
            newLog?.put(today, amount)
            newCount = fitness.count!!
        }

        println(differ)
        println(fitness.count!!)
        println(newCount)
        val fitnessCopy = fitness.copy(count = newCount, log = (newLog))
        // optimistically update the ui
        var index = uiState._fitness.indexOf(fitness)
        println(index)

        uiState._fitness[index] = fitnessCopy

        // go to Firebase
        FitnessRepository.updateFitness(fitnessCopy)
        uiState.currentFitness = uiState._fitness[index] // Update the selected exercise to the new copy of it
        uiState.buttonEnabled = true
    }

    suspend fun addFitnessCount(fitness: Fitness, count: Int) {
        val today = LocalDate.now().year.toString().plus("/").plus(LocalDate.now().monthValue.toString()).plus("/").plus(LocalDate.now().dayOfMonth.toString())

        val newLog = fitness.log
        if (newLog?.contains(today) == true) {
            newLog[today] = count + newLog[today]!!
        } else {
            newLog?.put(today, count)
        }

        val fitnessCopy = fitness.copy(count = (count + fitness.count!!), log = (newLog))
        // optimistically update the ui
        var index = uiState._fitness.indexOf(fitness)
        println(index)
        for(i in uiState._fitness) {
            println(i.name)
        }

        uiState._fitness[index] = fitnessCopy

        // go to Firebase
        FitnessRepository.updateFitness(fitnessCopy)
        uiState.currentFitness = uiState._fitness[index] // Update the selected exercise to the new copy of it
        uiState.buttonEnabled = true
    }



}