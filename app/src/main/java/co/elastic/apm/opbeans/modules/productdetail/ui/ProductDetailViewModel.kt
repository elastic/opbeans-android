package co.elastic.apm.opbeans.modules.productdetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.CartItemRepository
import co.elastic.apm.opbeans.app.data.repository.ProductRepository
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        viewModelScope.launch(MyDispatchers.Main) {
            cartItemRepository.addOrUpdateItem(productId)
            callback.invoke()
        }
    }
}