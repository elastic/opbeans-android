package co.elastic.apm.opbeans.modules.cart.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.repository.CartItemRepository
import co.elastic.apm.opbeans.app.data.repository.OrderRepository
import co.elastic.apm.opbeans.modules.cart.ui.state.CartCheckoutState
import co.elastic.apm.opbeans.modules.cart.ui.state.CartItemsLoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartItemRepository: CartItemRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val cartItems = mutableListOf<CartItem>()
    private val internalCartCheckoutState =
        MutableStateFlow<CartCheckoutState>(CartCheckoutState.Idle)
    val cartCheckoutState = internalCartCheckoutState.asStateFlow()
    val cartItemsLoadState: StateFlow<CartItemsLoadState> = cartItemRepository.getAllCartItems()
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
            try {
                internalCartCheckoutState.update { CartCheckoutState.Started }
                orderRepository.createOrder(1, cartItems)
                cartItemRepository.deleteAll()
                internalCartCheckoutState.update { CartCheckoutState.Finished }
            } catch (e: Throwable) {
                internalCartCheckoutState.update { CartCheckoutState.Error(e) }
            }
        }
    }
}