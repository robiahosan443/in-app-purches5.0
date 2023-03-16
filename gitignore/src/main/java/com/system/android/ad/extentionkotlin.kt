package com.system.android.ad

import android.view.View
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import kotlinx.coroutines.*

fun View.delayOnLifeCycle(
    durationInMills: Long,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block:() -> Unit): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
    lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
        delay(durationInMills)
        block()
    }
}