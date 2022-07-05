package co.elastic.apm.opbeans.app.data.source.product

import co.elastic.apm.opbeans.app.data.local.AppDatabase
import co.elastic.apm.opbeans.app.data.local.entities.ProductEntity
import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.source.product.helpers.ProductEntityMapper.toProduct
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class LocalProductSource @Inject constructor(private val appDatabase: AppDatabase) {

    private val productDao by lazy { appDatabase.productDao() }

    fun getProducts(): Flow<List<Product>> {
        return productDao.getAll().map {
            localListToProducts(it)
        }
    }

    suspend fun storeProducts(products: List<Product>) {
        val entities = products.map { productToEntity(it) }
        productDao.saveAll(entities)
    }

    private fun localListToProducts(localList: List<ProductEntity>): List<Product> {
        return localList.map { it.toProduct() }
    }

    private fun productToEntity(product: Product): ProductEntity {
        return ProductEntity(
            product.id,
            product.sku,
            product.name,
            product.stock,
            product.type
        )
    }
}