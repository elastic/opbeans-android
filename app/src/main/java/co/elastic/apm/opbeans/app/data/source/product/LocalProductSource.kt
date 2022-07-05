package co.elastic.apm.opbeans.app.data.source.product

import co.elastic.apm.opbeans.app.data.local.AppDatabase
import co.elastic.apm.opbeans.app.data.local.entities.ProductEntity
import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.source.product.helpers.ImageUrlBuilder
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class LocalProductSource @Inject constructor(private val appDatabase: AppDatabase) {

    private val productDao by lazy { appDatabase.productDao() }

    suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        productDao.getAll().map {
            localToProduct(it)
        }
    }

    private fun localToProduct(productEntity: ProductEntity): Product {
        return Product(
            productEntity.id,
            productEntity.sku,
            productEntity.name,
            productEntity.stock,
            productEntity.typeName,
            ImageUrlBuilder.build(productEntity.sku)
        )
    }
}