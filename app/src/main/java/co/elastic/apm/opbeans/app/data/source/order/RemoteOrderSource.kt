package co.elastic.apm.opbeans.app.data.source.order

import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.models.Order
import co.elastic.apm.opbeans.app.data.remote.OpBeansService
import co.elastic.apm.opbeans.app.data.remote.body.CreateOrderBody
import co.elastic.apm.opbeans.app.data.remote.body.OrderLine
import co.elastic.apm.opbeans.app.data.remote.models.RemoteOrder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class RemoteOrderSource @Inject constructor(private val opBeansService: OpBeansService) {

    companion object {
        private val remoteDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
        private val utcDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    }

    suspend fun createOrder(customerId: Int, items: List<CartItem>) = withContext(Dispatchers.IO) {
        val lines = items.map { OrderLine(it.product.id, it.amount) }
        opBeansService.createOrder(CreateOrderBody(customerId, lines))
    }

    suspend fun getOrders(): List<Order> = withContext(Dispatchers.IO) {
        opBeansService.getOrders().map {
            remoteToOrder(it)
        }
    }

    private fun remoteToOrder(remoteOrder: RemoteOrder): Order {
        val date = try {
            remoteDateFormat.parse(remoteOrder.createdAt)!!
        } catch (e: ParseException) {
            utcDateFormat.parse(remoteOrder.createdAt)!!
        }
        return Order(
            remoteOrder.id,
            remoteOrder.customerId,
            remoteOrder.customerName,
            date
        )
    }
}