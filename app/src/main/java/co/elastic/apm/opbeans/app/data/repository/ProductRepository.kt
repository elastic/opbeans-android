package co.elastic.apm.opbeans.app.data.repository

import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.source.RemoteProductSource

class ProductRepository(private val remoteProductSource: RemoteProductSource) {

    suspend fun getProducts(): List<Product> {
        return remoteProductSource.getProducts()
    }
}