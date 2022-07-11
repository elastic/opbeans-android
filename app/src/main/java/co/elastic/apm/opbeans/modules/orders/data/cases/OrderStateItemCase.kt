package co.elastic.apm.opbeans.modules.orders.data.cases

import co.elastic.apm.opbeans.app.data.models.Order
import co.elastic.apm.opbeans.app.data.repository.OrderRepository
import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem
import dagger.hilt.android.scopes.ViewModelScoped
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@ViewModelScoped
class OrderStateItemCase @Inject constructor(private val orderRepository: OrderRepository) {

    companion object {
        private val displayDateFormat =
            SimpleDateFormat("h:mm a '|' d MMM yyyy", Locale.getDefault())
    }

    suspend fun getSetOfOrders(offset: Int, amount: Int): List<OrderStateItem> {
        return orderRepository.getSetOfOrders(offset, amount)
            .map { orderToOrderStateItem(it) }
    }

    fun getAllCustomerOrders(customerId: Int): Flow<List<OrderStateItem>> {
        return orderRepository.getAllCustomerOrders(customerId)
            .map { list -> list.map { orderToOrderStateItem(it) } }
            .flowOn(Dispatchers.IO)
    }

    private fun orderToOrderStateItem(order: Order): OrderStateItem {
        return OrderStateItem(
            order.id,
            "#${order.id}",
            order.customerName,
            displayDateFormat.format(order.createdAt)
        )
    }
}