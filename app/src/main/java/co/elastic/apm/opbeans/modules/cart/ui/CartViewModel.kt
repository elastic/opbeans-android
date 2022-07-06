package co.elastic.apm.opbeans.modules.cart.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.CartItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class CartViewModel @Inject constructor(private val cartItemRepository: CartItemRepository) :
    ViewModel() {

    val cartState: StateFlow<CartViewState> = cartItemRepository.getAllCartItems()
        .catch { e -> CartViewState.ErrorLoading(e) }
        .map { CartViewState.FinishedLoading(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000), CartViewState.Loading
        )

    fun doCheckout() {
        cartItemRepository.removeItems()
    }
}