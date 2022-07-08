package co.elastic.apm.opbeans.app.data.repository

import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.models.Order
import co.elastic.apm.opbeans.app.data.source.order.LocalOrderSource
import co.elastic.apm.opbeans.app.data.source.order.RemoteOrderSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class OrderRepository @Inject constructor(
    private val remoteOrderSource: RemoteOrderSource,
    private val localOrderSource: LocalOrderSource
) {

    suspend fun createOrder(customerId: Int, items: List<CartItem>) {
        remoteOrderSource.createOrder(customerId, items)
    }

    suspend fun getSetOfOrders(offset: Int, amount: Int): List<Order> {
        return localOrderSource.getSetOfOrders(offset, amount)
    }

    suspend fun fetchOrders() = withContext(Dispatchers.IO) {
        val remoteOrders = remoteOrderSource.getOrders()
        localOrderSource.saveAll(remoteOrders)
    }
}