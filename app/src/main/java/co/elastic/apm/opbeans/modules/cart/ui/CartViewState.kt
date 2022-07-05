package co.elastic.apm.opbeans.modules.cart.ui

import co.elastic.apm.opbeans.app.data.models.CartItem

sealed class CartViewState {
    object Loading : CartViewState()
    class FinishedLoading(val items: List<CartItem>) : CartViewState()
    class ErrorLoading(val e: Throwable) : CartViewState()
}