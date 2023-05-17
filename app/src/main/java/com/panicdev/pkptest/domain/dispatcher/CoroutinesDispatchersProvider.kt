package com.panicdev.pkptest.domain.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutinesDispatchersProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
}
