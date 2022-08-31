package co.elastic.apm.opbeans.modules.cart.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.auth.AuthManager
import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.repository.CartItemRepository
import co.elastic.apm.opbeans.app.data.repository.OrderRepository
import co.elastic.apm.opbeans.app.tools.EventFlow
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import co.elastic.apm.opbeans.app.tools.update
import co.elastic.apm.opbeans.modules.cart.ui.state.CartCheckoutState
import co.elastic.apm.opbeans.modules.cart.ui.state.CartItemsLoadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val cartItemRepository: CartItemRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private var fetchListJob: Job? = null
    private val cartItems = mutableListOf<CartItem>()
    private val internalCartCheckoutState = EventFlow<CartCheckoutState>(CartCheckoutState.Idle)
    val cartCheckoutState = internalCartCheckoutState.asSharedFlow()

    private val internalCartItemsLoadState =
        EventFlow<CartItemsLoadState>(CartItemsLoadState.Loading)
    val cartItemsLoadState: SharedFlow<CartItemsLoadState> =
        internalCartItemsLoadState.asSharedFlow()
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000))

    fun fetchData() {
        fetchListJob?.cancel()
        fetchListJob = viewModelScope.launch(MyDispatchers.Main) {
            try {
                internalCartItemsLoadState.update { CartItemsLoadState.Loading }
                cartItemRepository.getAllCartItems()
                    .catch { e -> CartItemsLoadState.ErrorLoading(e) }
                    .onEach { interceptItems(it) }
                    .collectLatest {
                        internalCartItemsLoadState.update { CartItemsLoadState.FinishedLoading(it) }
                    }
            } catch (e: Throwable) {
                internalCartItemsLoadState.update { CartItemsLoadState.ErrorLoading(e) }
            }
        }
    }

    fun doCheckout() {
        if (cartItems.isEmpty()) {
            internalCartCheckoutState.update { CartCheckoutState.NoItemsToCheckout }
            return
        }
        viewModelScope.launch(MyDispatchers.Main) {
            try {
                internalCartCheckoutState.update { CartCheckoutState.Started }
                orderRepository.createOrder(authManager.getUser(), cartItems)
                cartItemRepository.deleteAll()
                internalCartCheckoutState.update { CartCheckoutState.Finished }
            } catch (e: Throwable) {
                internalCartCheckoutState.update { CartCheckoutState.Error(e) }
            }
        }
    }

    private fun interceptItems(items: List<CartItem>) {
        cartItems.clear()
        cartItems.addAll(items)
    }
}