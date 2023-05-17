package com.panicdev.pkptest.presentation.mvi

import androidx.annotation.MainThread

@Suppress("UNUSED_PARAMETER")
fun <T> asyncHandler(
    state: Async<T>,
    onRetryAction: () -> Unit,
    error: (Throwable) -> Unit = { },
    loading: () -> Unit = { },
    uninitialized: () -> Unit = { },
    success: (T) -> Unit,
) {
    when (state) {
        Uninitialized -> uninitialized()
        is Loading -> loading()
        is Fail -> error(state.error)
        is Success -> success(state())
    }
}

/**
 * Usage:
 * result.doOnLoading {
 *     showLoading()
 * }
 */
inline fun <T> Async<T>.doOnLoading(operation: () -> Unit) {
    when (this) {
        Uninitialized -> {}
        is Loading -> operation()
        is Fail -> {}
        is Success -> {}
    }
}


inline fun <T> Async<T>.doOnSuccess(operation: (T) -> Unit) {
    when (this) {
        Uninitialized -> {}
        is Success -> operation(this())
        is Fail -> {}
        is Loading -> {}
    }
}

@MainThread
inline fun <T> Async<T>.renderState(
    crossinline onLoading: () -> Unit = {},
    crossinline onError: (String?) -> Unit = {},
    crossinline onSuccess: (T) -> Unit = {},
) {
    when (this) {
        Uninitialized -> {}
        is Success -> onSuccess(this())
        is Fail -> onError(error.message)
        is Loading -> onLoading.invoke()
    }
}
