package co.elastic.apm.opbeans.app.data.repository

import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.source.product.LocalProductSource
import co.elastic.apm.opbeans.app.data.source.product.RemoteProductSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Singleton
class ProductRepository @Inject constructor(
    private val remoteProductSource: RemoteProductSource,
    private val localProductSource: LocalProductSource
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    fun getProducts(): Flow<List<Product>> {
        fetchRemoteProducts()
        return localProductSource.getProducts()
    }

    private fun fetchRemoteProducts() {
        scope.launch {
            val remoteProducts = remoteProductSource.getProducts()
            localProductSource.storeProducts(remoteProducts)
        }
    }
}