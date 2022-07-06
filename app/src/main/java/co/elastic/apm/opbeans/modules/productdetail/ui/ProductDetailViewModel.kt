package co.elastic.apm.opbeans.modules.productdetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.CartItemRepository
import co.elastic.apm.opbeans.app.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartItemRepository: CartItemRepository
) :
    ViewModel() {

    private val internalState = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val state = internalState.asStateFlow()

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
            cartItemRepository.addItem(productId)
        }
    }
}