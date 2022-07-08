package co.elastic.apm.opbeans.app.data.source.order

import co.elastic.apm.opbeans.app.data.local.AppDatabase
import co.elastic.apm.opbeans.app.data.local.entities.OrderEntity
import co.elastic.apm.opbeans.app.data.models.Order
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@Singleton
class LocalOrderSource @Inject constructor(private val appDatabase: AppDatabase) {

    private val orderDao by lazy { appDatabase.orderDao() }

    fun getOrders(): Flow<List<Order>> {
        return orderDao.getOrders()
            .map { list -> list.map { entityToOrder(it) } }
            .flowOn(Dispatchers.IO)
    }

    private fun entityToOrder(orderEntity: OrderEntity): Order {
        return Order(
            orderEntity.id,
            orderEntity.customerId,
            orderEntity.customerName,
            Date(orderEntity.createdAt)
        )
    }
}