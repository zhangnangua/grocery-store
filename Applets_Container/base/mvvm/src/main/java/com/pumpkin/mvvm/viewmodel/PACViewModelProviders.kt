package com.pumpkin.mvvm.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

object PACViewModelProviders {

    fun of(owner: ViewModelStoreOwner) = ViewModelProvider(
        owner,
        ViewModelProvider.NewInstanceFactory()
    )

    fun of(owner: ViewModelStore) = ViewModelProvider(
        owner,
        ViewModelProvider.NewInstanceFactory()
    )
}