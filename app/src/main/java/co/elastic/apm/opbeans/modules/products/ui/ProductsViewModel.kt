package co.elastic.apm.opbeans.modules.products.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private val internalState: MutableStateFlow<ProductsState> =
        MutableStateFlow(ProductsState.Loading)
    val state = internalState.asStateFlow()

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                internalState.update { ProductsState.Loading }
                productRepository.getProducts().stateIn(
                    viewModelScope, SharingStarted.WhileSubscribed(5000),
                    emptyList()
                ).collectLatest { products ->
                    internalState.update { ProductsState.ProductsLoaded(products) }
                }
                productRepository.fetchRemoteProducts()
            } catch (e: Exception) {
                internalState.update { ProductsState.Error(e) }
            }
        }
    }
}