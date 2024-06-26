package com.joshi.mvvmwithjetpackcompose.model.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver{
    fun observe() : Flow<Status>

    enum class Status{
        Available,
        UnAvailable
    }
}