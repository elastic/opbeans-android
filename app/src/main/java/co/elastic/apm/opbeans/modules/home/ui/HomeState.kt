package co.elastic.apm.opbeans.modules.home.ui

import co.elastic.apm.opbeans.app.data.models.Product

sealed class HomeState {
    object Loading : HomeState()
    object Error : HomeState()
    class ProductsLoaded(val products: List<Product>) : HomeState()
}