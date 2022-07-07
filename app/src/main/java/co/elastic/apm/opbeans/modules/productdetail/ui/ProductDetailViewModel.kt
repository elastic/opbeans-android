package co.elastic.apm.opbeans.modules.productdetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.CartItemRepository
import co.elastic.apm.opbeans.app.data.repository.ProductRepository
import co.elastic.apm.opbeans.app.data.source.cart.exceptions.ProductAlreadyInCartException
import co.elastic.apm.opbeans.app.tools.EventFlow
import co.elastic.apm.opbeans.app.tools.update
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartItemRepository: CartItemRepository
) : ViewModel() {

    private val internalState = EventFlow<ProductDetailState>(ProductDetailState.Loading)
    val state = internalState.asSharedFlow()

    fun fetchProduct(productId: Int) {
        viewModelScope.launch {
            internalState.update { ProductDetailState.Loading }
            try {
                val product = productRepository.getProductById(productId)
                internalState.update { ProductDetailState.FinishedLoading(product) }
            } catch (e: Throwable) {
                internalState.update { ProductDetailState.ErrorLoading(e) }
            }
        }
    }

    fun addProductToCart(productId: Int) {
        viewModelScope.launch {
            try {
                cartItemRepository.addItem(productId)
                internalState.update { ProductDetailState.AddedToCart }
            } catch (e: ProductAlreadyInCartException) {
                internalState.update { ProductDetailState.AlreadyInCart }
            }
        }
    }
}