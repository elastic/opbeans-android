package co.elastic.apm.opbeans.app.tools

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object MyDispatchers {
    var Main: CoroutineDispatcher = Dispatchers.Main
    var IO: CoroutineDispatcher = Dispatchers.IO
    var Default: CoroutineDispatcher = Dispatchers.Default
}