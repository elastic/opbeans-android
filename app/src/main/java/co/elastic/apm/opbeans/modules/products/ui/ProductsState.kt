package co.elastic.apm.opbeans.modules.products.ui

import co.elastic.apm.opbeans.app.data.models.Product

sealed class ProductsState {
    object Loading : ProductsState()
    class Error(val e: Exception) : ProductsState()
    class ProductsLoaded(val products: List<Product>) : ProductsState()
}