package co.elastic.apm.opbeans.app.data.source.product.helpers

import co.elastic.apm.opbeans.BuildConfig

object ImageUrlBuilder {

    fun build(sku: String): String {
        return "${BuildConfig.BASE_URL}/images/products/$sku.jpg"
    }
}