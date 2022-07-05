package co.elastic.apm.opbeans.modules.products.ui.state

sealed class NetworkRequestState {
    object Running : NetworkRequestState()
    object Successful : NetworkRequestState()
    class Failed(val e: Throwable) : NetworkRequestState()
}
