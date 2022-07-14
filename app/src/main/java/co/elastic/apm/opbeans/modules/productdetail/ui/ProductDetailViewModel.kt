package co.elastic.apm.opbeans.modules.productdetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.CartItemRepository
import co.elastic.apm.opbeans.app.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartItemRepository: CartItemRepository
) : ViewModel() {

    fun fetchProduct(productId: Int, callback: (ProductDetailLoadState) -> Unit) {
        callback.invoke(ProductDetailLoadState.Loading)
        productRepository.getProductById(productId) { result ->
            if (result.isSuccess) {
                callback.invoke(ProductDetailLoadState.FinishedLoading(result.getOrThrow()))
            } else {
                callback.invoke(ProductDetailLoadState.ErrorLoading(result.exceptionOrNull()!!))
            }
        }
    }

    fun addProductToCart(productId: Int, callback: () -> Unit) {
        viewModelScope.launch {
            cartItemRepository.addOrUpdateItem(productId)
            callback.invoke()
        }
    }
}