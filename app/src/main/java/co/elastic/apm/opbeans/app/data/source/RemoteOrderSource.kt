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
        private val remoteDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
        private val displayDateFormat =
            SimpleDateFormat("h:mm a '|' d MMM yyyy", Locale.getDefault())
    }

    suspend fun getOrders(): List<Order> = withContext(Dispatchers.IO) {
        opBeansService.getOrders().map {
            remoteToOrder(it)
        }
    }

    private fun remoteToOrder(remoteOrder: RemoteOrder): Order {
        val date = remoteDateFormat.parse(remoteOrder.createdAt)!!
        return Order(
            remoteOrder.id,
            remoteOrder.customerName,
            date,
            displayDateFormat.format(date)
        )
    }
}