package co.elastic.apm.opbeans.app.data.source.product.helpers

import co.elastic.apm.opbeans.app.data.local.entities.ProductEntity
import co.elastic.apm.opbeans.app.data.models.Product

object ProductEntityMapper {

    fun ProductEntity.toProduct(): Product {
        return Product(
            id,
            sku,
            name,
            stock,
            typeName,
            ImageUrlBuilder.build(sku)
        )
    }
}