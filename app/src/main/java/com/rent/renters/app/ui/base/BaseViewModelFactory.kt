package com.rent.renters.app.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BaseViewModelFactory  constructor(
        private val baseViewModel: BaseViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaseViewModel::class.java)) {
            return baseViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}