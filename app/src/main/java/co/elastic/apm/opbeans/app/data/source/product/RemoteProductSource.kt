package co.elastic.apm.opbeans.app.data.source.product

import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.remote.OpBeansService
import co.elastic.apm.opbeans.app.data.remote.models.RemoteProduct
import co.elastic.apm.opbeans.app.data.source.product.helpers.ImageUrlBuilder
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class RemoteProductSource @Inject constructor(private val opBeansService: OpBeansService) {

    suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        opBeansService.getProducts().map {
            remoteToProduct(it)
        }
    }

    private fun remoteToProduct(remoteProduct: RemoteProduct): Product {
        return Product(
            remoteProduct.id,
            remoteProduct.sku,
            remoteProduct.name,
            remoteProduct.stock,
            remoteProduct.typeName,
            ImageUrlBuilder.build(remoteProduct.sku)
        )
    }
}