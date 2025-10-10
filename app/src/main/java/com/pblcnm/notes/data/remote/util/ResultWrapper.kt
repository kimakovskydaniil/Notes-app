package com.pblcnm.notes.data.remote.util

sealed class ResultWrapper<out R> {
    data class Success<out R>(val payload: R) : ResultWrapper<R>()
    data class Error(val exception: Throwable) : ResultWrapper<Nothing>()

    inline fun <T> mapSuccess(transform: (R) -> T): ResultWrapper<T> {
        return when (this) {
            is Success -> try {
                Success(transform(payload))
            } catch (ex: Throwable) {
                Error(ex)
            }
            is Error -> this
        }
    }

    inline fun handleError(action: (Throwable) -> Unit): ResultWrapper<R> {
        if (this is Error) action(exception)
        return this
    }
}