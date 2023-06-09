package com.panicdev.pkptest.presentation.mvi

import org.orbitmvi.orbit.syntax.simple.SimpleContext
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

interface AsyncContext<STATE : Any, SIDE_EFFECT : Any, RESOURCE : Any> {
    suspend fun execute(
        cachedValue: Async<RESOURCE>? = null,
        reducer: SimpleContext<STATE>.(Async<RESOURCE>) -> STATE,
    )
}

/**
 * @see [AsyncContext]
 */
internal class AsyncContextImpl<STATE : Any, SIDE_EFFECT : Any, RESOURCE : Any>(
    private val action: suspend (STATE) -> RESOURCE,
    private val simpleSyntaxContext: SimpleSyntax<STATE, SIDE_EFFECT>,
) : AsyncContext<STATE, SIDE_EFFECT, RESOURCE> {
    override suspend fun execute(
        cachedValue: Async<RESOURCE>?,
        reducer: SimpleContext<STATE>.(Async<RESOURCE>) -> STATE,
    ) {
        try {
            simpleSyntaxContext.reduce { reducer(Loading(cachedValue?.invoke())) }
            val result = action(simpleSyntaxContext.state)
            simpleSyntaxContext.reduce { reducer(Success(result)) }
        } catch (_: CancellationException) {
            Timber.d("AsyncActions has been cancelled")
        } catch (ex: Throwable) {
            Timber.e("AsyncActions Error ", ex)
            simpleSyntaxContext.reduce { reducer(Fail(ex, cachedValue?.invoke())) }
        }
    }
}

/**
 * Creates an [AsyncContext] instance that will handle all [Async] state for you
 *
 * @see [AsyncContext]
 */
fun <STATE : Any, SIDE_EFFECT : Any, RESOURCE : Any> SimpleSyntax<STATE, SIDE_EFFECT>.async(action: suspend (STATE) -> RESOURCE): AsyncContext<STATE, SIDE_EFFECT, RESOURCE> {
    return AsyncContextImpl(action, this)
}
