package com.cadenharris.fitness.ui.viewmodels


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.cadenharris.fitness.ui.models.Fitness
import com.cadenharris.fitness.ui.repositories.FitnessRepository
import java.time.LocalDate

class FitnessModificationScreenState {
    var name by mutableStateOf("")
    var usage by mutableStateOf(Fitness.USAGE_TRACKING)
    var count by mutableStateOf("0")
    var description by mutableStateOf("")

    var titleError by mutableStateOf(false)
    var boxChecked by mutableStateOf(false)
    var errorMessage by mutableStateOf("")
    var saveSuccess by mutableStateOf(false)

    val today = LocalDate.now().year.toString().plus("/").plus(LocalDate.now().monthValue.toString()).plus("/").plus(LocalDate.now().dayOfMonth.toString())
}

class FitnessModificationViewModel(application: Application): AndroidViewModel(application) {
    val uiState = FitnessModificationScreenState()
    var id: String? = null
    suspend fun setupInitialState(id: String?) {
        if (id == null || id == "new") return
        this.id = id
        val fitness = FitnessRepository.getFitness().find { it.id == id } ?: return
        // note: handle fitness that isn't found
        uiState.name = fitness.name ?: ""
        uiState.description = fitness.description ?: ""
        uiState.count = "${fitness.count ?: "0"}"
        if(fitness.usage == Fitness.USAGE_PERSONAL) {
            uiState.boxChecked = true
        }

    }

    suspend fun deleteFitness(fitnessId: String) {
        val fitness = FitnessRepository.getFitness().find { it.id == id } ?: return
        FitnessRepository.deleteFitness(fitness)
    }

    suspend fun saveFitness() {

        uiState.errorMessage = ""
        uiState.titleError = false


        if (uiState.name.isEmpty()) {
            uiState.titleError = true
            uiState.errorMessage = "Name cannot be blank."
            return
        }
        val today = uiState.today
        if (uiState.boxChecked) {
            uiState.usage = Fitness.USAGE_PERSONAL
        }
        val map = mutableMapOf<String,Int>(today to 0)
        if (id == null) { // create new
            FitnessRepository.createFitness(
                uiState.name,
                uiState.description,
                uiState.count.toInt(),
                uiState.usage,
                today,
                map,
            )
        } else { // update
            val fitness = FitnessRepository.getFitness().find { it.id == id } ?: return
            FitnessRepository.updateFitness(
                fitness.copy(
                    name = uiState.name,
                    description = uiState.description,
                    count = uiState.count.toInt(),
                    usage = uiState.usage
                )
            )
        }

        uiState.saveSuccess = true
    }
}