package co.elastic.apm.opbeans.modules.cart.ui.state

import co.elastic.apm.opbeans.app.data.models.CartItem

sealed class CartItemsLoadState {
    object Loading : CartItemsLoadState()
    class FinishedLoading(val items: List<CartItem>) : CartItemsLoadState()
    class ErrorLoading(val e: Throwable) : CartItemsLoadState()
}