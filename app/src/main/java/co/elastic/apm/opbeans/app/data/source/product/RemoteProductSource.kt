package co.elastic.apm.opbeans.app.data.source.product

import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.models.ProductDetail
import co.elastic.apm.opbeans.app.data.remote.OpBeansService
import co.elastic.apm.opbeans.app.data.remote.models.RemoteProduct
import co.elastic.apm.opbeans.app.data.remote.models.RemoteProductDetail
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

    suspend fun getProductById(id: Int): ProductDetail = withContext(Dispatchers.IO) {
        remoteToProductDetail(opBeansService.getProductById(id))
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

    private fun remoteToProductDetail(remoteProduct: RemoteProductDetail): ProductDetail {
        return ProductDetail(
            remoteProduct.id,
            remoteProduct.sku,
            remoteProduct.name,
            remoteProduct.description,
            remoteProduct.typeName,
            remoteProduct.stock,
            remoteProduct.sellingPrice,
            ImageUrlBuilder.build(remoteProduct.sku)
        )
    }
}