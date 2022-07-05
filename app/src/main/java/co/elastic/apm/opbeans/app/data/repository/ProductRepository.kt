package co.elastic.apm.opbeans.app.data.repository

import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.source.product.RemoteProductSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(private val remoteProductSource: RemoteProductSource) {

    suspend fun getProducts(): List<Product> {
        return remoteProductSource.getProducts()
    }
}