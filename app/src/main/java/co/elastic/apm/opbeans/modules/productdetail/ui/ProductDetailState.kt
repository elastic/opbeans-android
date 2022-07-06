package co.elastic.apm.opbeans.modules.productdetail.ui

import co.elastic.apm.opbeans.app.data.models.Product

sealed class ProductDetailState {
    object Loading : ProductDetailState()
    class FinishedLoading(val product: Product) : ProductDetailState()
    class ErrorLoading(val e: Throwable) : ProductDetailState()
}