package co.elastic.apm.opbeans.app.data.repository

import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.models.Order
import co.elastic.apm.opbeans.app.data.source.RemoteOrderSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(private val remoteOrderSource: RemoteOrderSource) {

    suspend fun createOrder(customerId: Int, items: List<CartItem>) {
        remoteOrderSource.createOrder(customerId, items)
    }

    suspend fun getOrders(): List<Order> {
        return remoteOrderSource.getOrders()
    }
}