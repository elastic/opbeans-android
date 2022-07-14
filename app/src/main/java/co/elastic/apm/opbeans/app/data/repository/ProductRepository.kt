package co.elastic.apm.opbeans.app.data.repository

import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.models.ProductDetail
import co.elastic.apm.opbeans.app.data.source.product.LocalProductSource
import co.elastic.apm.opbeans.app.data.source.product.RemoteProductSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Singleton
class ProductRepository @Inject constructor(
    private val remoteProductSource: RemoteProductSource,
    private val localProductSource: LocalProductSource
) {

    fun getProducts(): Flow<List<Product>> {
        return localProductSource.getProducts()
    }

    fun getProductById(id: Int, callback: (Result<ProductDetail>) -> Unit) {
        return remoteProductSource.getProductById(id, callback)
    }

    suspend fun fetchRemoteProducts() = withContext(Dispatchers.IO) {
        val remoteProducts = remoteProductSource.getProducts()
        localProductSource.storeProducts(remoteProducts)
    }
}