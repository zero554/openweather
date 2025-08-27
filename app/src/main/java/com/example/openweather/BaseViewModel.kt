package com.example.openweather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {
    private val channel: Channel<Any> = Channel()
    val uiEvents: Flow<Any> = channel.receiveAsFlow()

    protected fun runCoroutine(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context) { block() }

    protected fun postUiEvent(event: Any) = runCoroutine {
        channel.send(event)
    }
}