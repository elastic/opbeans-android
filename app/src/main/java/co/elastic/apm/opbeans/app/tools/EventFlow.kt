package co.elastic.apm.opbeans.app.tools

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

fun <T> EventFlow(initialValue: T): MutableSharedFlow<T> = MutableSharedFlow<T>(
    replay = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
).also {
    it.tryEmit(initialValue)
}

fun <T> MutableSharedFlow<T>.update(function: () -> T) {
    tryEmit(function.invoke())
}