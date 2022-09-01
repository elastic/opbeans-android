package co.elastic.apm.opbeans.app.data.repository

import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.data.models.Order
import co.elastic.apm.opbeans.app.data.models.OrderDetail
import co.elastic.apm.opbeans.app.data.source.order.LocalOrderSource
import co.elastic.apm.opbeans.app.data.source.order.RemoteOrderSource
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val remoteOrderSource: RemoteOrderSource,
    private val localOrderSource: LocalOrderSource
) {

    suspend fun createOrder(customer: Customer, items: List<CartItem>) {
        val result = remoteOrderSource.createOrder(customer.id, items)
        localOrderSource.saveOrder(Order(result.orderId, customer.id, customer.fullName, Date()))
    }

    suspend fun getOrderDetails(orderId: Int): OrderDetail {
        return remoteOrderSource.getOrderDetails(orderId)
    }

    suspend fun getSetOfOrders(offset: Int, amount: Int): List<Order> {
        return localOrderSource.getSetOfOrders(offset, amount)
    }

    suspend fun fetchOrders() = withContext(MyDispatchers.IO) {
        val remoteOrders = remoteOrderSource.getOrders()
        localOrderSource.saveAll(remoteOrders)
    }

    fun getAllCustomerOrders(customerId: Int): Flow<List<Order>> {
        return localOrderSource.getAllCustomerOrders(customerId)
    }

    suspend fun getTotalAmountOfOrders(): Int {
        return localOrderSource.getTotalAmountOfOrders()
    }
}