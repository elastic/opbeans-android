package co.elastic.apm.opbeans.app.data.source

import co.elastic.apm.opbeans.app.data.models.Order
import co.elastic.apm.opbeans.app.data.remote.OpBeansService
import co.elastic.apm.opbeans.app.data.remote.models.RemoteOrder
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class RemoteOrderSource @Inject constructor(private val opBeansService: OpBeansService) {

    companion object {
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
    }

    suspend fun getOrders(): List<Order> = withContext(Dispatchers.IO) {
        opBeansService.getOrders().map {
            remoteToOrder(it)
        }
    }

    private fun remoteToOrder(remoteOrder: RemoteOrder): Order {
        return Order(
            remoteOrder.id,
            remoteOrder.customerName,
            dateFormat.parse(remoteOrder.createdAt)!!
        )
    }
}