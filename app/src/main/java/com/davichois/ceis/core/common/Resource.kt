package com.davichois.ceis.core.common

sealed class Resource<out T> {
    object PreLoad : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}