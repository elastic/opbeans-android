package co.elastic.apm.opbeans.modules.productdetail.ui

import co.elastic.apm.opbeans.app.data.models.ProductDetail

sealed class ProductDetailState {
    object Loading : ProductDetailState()
    object AddedToCart : ProductDetailState()
    class FinishedLoading(val product: ProductDetail) : ProductDetailState()
    class ErrorLoading(val e: Throwable) : ProductDetailState()
}