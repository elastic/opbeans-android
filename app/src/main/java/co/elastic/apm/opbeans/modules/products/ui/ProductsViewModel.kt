package co.elastic.apm.opbeans.modules.products.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.ProductRepository
import co.elastic.apm.opbeans.modules.products.ui.state.NetworkRequestState
import co.elastic.apm.opbeans.modules.products.ui.state.ProductsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private val internalNetworkRequestState =
        MutableStateFlow<NetworkRequestState>(NetworkRequestState.Running)
    val networkRequestState: StateFlow<NetworkRequestState> =
        internalNetworkRequestState.asStateFlow()
    val products: StateFlow<ProductsState> = productRepository.getProducts()
        .catch { e ->
            ProductsState.Error(e)
        }.map {
            ProductsState.ProductsLoaded(it)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductsState.Loading)

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                internalNetworkRequestState.update { NetworkRequestState.Running }
                productRepository.fetchRemoteProducts()
                internalNetworkRequestState.update { NetworkRequestState.Successful }
            } catch (e: Exception) {
                internalNetworkRequestState.update { NetworkRequestState.Failed(e) }
            }
        }
    }
}