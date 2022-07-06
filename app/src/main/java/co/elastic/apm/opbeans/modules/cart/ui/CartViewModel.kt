package co.elastic.apm.opbeans.modules.cart.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.repository.CartItemRepository
import co.elastic.apm.opbeans.modules.cart.ui.state.CartItemsLoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class CartViewModel @Inject constructor(private val cartItemRepository: CartItemRepository) :
    ViewModel() {

    private val cartItems = mutableListOf<CartItem>()
    val cartState: StateFlow<CartItemsLoadState> = cartItemRepository.getAllCartItems()
        .catch { e -> CartItemsLoadState.ErrorLoading(e) }
        .map { CartItemsLoadState.FinishedLoading(it) }
        .onEach { interceptItems(it.items) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), CartItemsLoadState.Loading
        )

    private fun interceptItems(items: List<CartItem>) {
        cartItems.clear()
        cartItems.addAll(items)
    }

    fun doCheckout() {
        viewModelScope.launch {
            cartItemRepository.deleteAll()
        }
    }
}