package co.elastic.apm.opbeans.app.data.source.order

import co.elastic.apm.opbeans.app.data.local.AppDatabase
import co.elastic.apm.opbeans.app.data.local.entities.OrderEntity
import co.elastic.apm.opbeans.app.data.models.Order
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class LocalOrderSource @Inject constructor(private val appDatabase: AppDatabase) {

    private val orderDao by lazy { appDatabase.orderDao() }

    suspend fun getSetOfOrders(offset: Int, amount: Int): List<Order> =
        withContext(Dispatchers.IO) {
            orderDao.getSetOfOrders(offset, amount).map { entityToOrder(it) }
        }

    suspend fun saveAll(orders: List<Order>) = withContext(Dispatchers.IO) {
        orderDao.insertAll(orders.map { order -> orderToEntity(order) })
    }

    suspend fun getCustomerOrders(customerId: Int, offset: Int, amount: Int): List<Order> =
        withContext(Dispatchers.IO) {
            orderDao.getCustomerOrders(customerId, offset, amount).map { entityToOrder(it) }
        }

    private fun orderToEntity(order: Order): OrderEntity {
        return OrderEntity(
            order.id,
            order.customerId,
            order.customerName,
            order.createdAt.time
        )
    }

    private fun entityToOrder(orderEntity: OrderEntity): Order {
        return Order(
            orderEntity.id,
            orderEntity.customerId,
            orderEntity.customerName,
            Date(orderEntity.createdAt)
        )
    }

    suspend fun getTotalAmountOfOrders(): Int {
        return orderDao.getOrderRowCount()
    }
}