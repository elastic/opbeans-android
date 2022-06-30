package co.elastic.apm.opbeans.app.data.source

import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.remote.OpBeansService
import co.elastic.apm.opbeans.app.data.remote.models.RemoteProduct
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteProductSource @Inject constructor(private val opBeansService: OpBeansService) {

    suspend fun getProducts(): List<Product> {
        return opBeansService.getProducts().map {
            remoteToProduct(it)
        }
    }

    private fun remoteToProduct(remoteProduct: RemoteProduct): Product {
        return Product(
            remoteProduct.id,
            remoteProduct.sku,
            remoteProduct.name,
            remoteProduct.stock,
            remoteProduct.typeName
        )
    }
}