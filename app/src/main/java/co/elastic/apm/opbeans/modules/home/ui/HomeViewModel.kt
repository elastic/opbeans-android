package co.elastic.apm.opbeans.modules.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val internalState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Loading)
    val state = internalState.asStateFlow()

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                internalState.update { HomeState.Loading }
                val products = productRepository.getProducts()
                internalState.update { HomeState.ProductsLoaded(products) }
            } catch (e: Exception) {
                internalState.update { HomeState.Error }
            }
        }
    }
}