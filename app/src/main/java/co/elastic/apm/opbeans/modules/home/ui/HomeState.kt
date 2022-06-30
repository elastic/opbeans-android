package co.elastic.apm.opbeans.modules.home.ui

import co.elastic.apm.opbeans.app.data.models.Product

sealed class HomeState {
    object Loading : HomeState()
    class Error(val e: Exception) : HomeState()
    class ProductsLoaded(val products: List<Product>) : HomeState()
}