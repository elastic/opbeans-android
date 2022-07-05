package co.elastic.apm.opbeans.modules.products.ui.state

import co.elastic.apm.opbeans.app.data.models.Product

sealed class ProductsState {
    object Loading : ProductsState()
    class Error(val e: Throwable) : ProductsState()
    class ProductsLoaded(val products: List<Product>) : ProductsState()
}