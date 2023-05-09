package com.cadenharris.fitness.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cadenharris.fitness.ui.repositories.UserRepository


class RootNavigationViewModel(application: Application): AndroidViewModel(application) {
    fun signOutUser() {
        UserRepository.signOutUser()
    }
}