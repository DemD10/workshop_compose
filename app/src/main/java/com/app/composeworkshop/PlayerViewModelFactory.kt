package com.app.composeworkshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController

class PlayerViewModelFactory(private val navHostController: NavHostController) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NavHostController::class.java)
            .newInstance(navHostController)
    }
}