package co.elastic.apm.opbeans.modules.cart.ui.state

sealed class CartCheckoutState {
    object Idle : CartCheckoutState()
    object Started : CartCheckoutState()
    object Finished : CartCheckoutState()
    class Error(val e: Throwable) : CartCheckoutState()
}