package co.elastic.apm.opbeans.modules.productdetail.ui

import co.elastic.apm.opbeans.app.data.models.ProductDetail

sealed class ProductDetailLoadState {
    object Loading : ProductDetailLoadState()
    class FinishedLoading(val product: ProductDetail) : ProductDetailLoadState()
    class ErrorLoading(val e: Throwable) : ProductDetailLoadState()
}