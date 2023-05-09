package com.cadenharris.fitness.ui.repositories

import com.cadenharris.fitness.ui.models.Fitness
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*

object FitnessRepository {

    private val fitnessCache = mutableListOf<Fitness>()
    private var cacheInitialized = false

    suspend fun getFitness(): List<Fitness> {
        if (!cacheInitialized) {
            val snapshot = Firebase.firestore
                .collection("fitness")
                .whereEqualTo("userId", UserRepository.getCurrentUserId())
                .get()
                .await()
            fitnessCache.addAll(snapshot.toObjects())
            cacheInitialized = true
        }
        return fitnessCache
    }
    suspend fun createFitness (
        name: String,
        description: String,
        count: Int,
        usage: Int,
        date: String,
        log: MutableMap<String, Int>,

    ): Fitness {
        val doc = Firebase.firestore.collection("fitness").document()
        val fitness = Fitness(
            name = name,
            description = description,
            count = count,
            usage = usage,
            id = doc.id,
            userId = UserRepository.getCurrentUserId(),
            date = date,
            log = log,
        )

        doc.set(fitness).await()
        fitnessCache.add(fitness)
        return fitness
    }

    suspend fun deleteFitness(fitness: Fitness) {
        Firebase.firestore.collection("fitness")
            .document(fitness.id!!)
            .delete()
            .addOnSuccessListener { println("DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { println("Error deleting document") }

        fitnessCache.remove(fitness)
    }

    suspend fun updateFitness(fitness: Fitness) {
        // write over the existing document with the new object
        Firebase.firestore
            .collection("fitness")
            .document(fitness.id!!)
            .set(fitness)
            .await()

        // update cache
        val oldFitnessIndex = fitnessCache.indexOfFirst {
            it.id == fitness.id
        }
        fitnessCache[oldFitnessIndex] = fitness
    }
}