package co.elastic.apm.opbeans.modules.orderdetail.data.usecases

import co.elastic.apm.opbeans.app.data.models.OrderDetail
import co.elastic.apm.opbeans.app.data.models.OrderedProduct
import co.elastic.apm.opbeans.app.data.repository.OrderRepository
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import co.elastic.apm.opbeans.modules.orderdetail.data.OrderDetailStateItem
import co.elastic.apm.opbeans.modules.orderdetail.data.OrderedProductSateItem
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@ViewModelScoped
class OrderDetailStateUseCase @Inject constructor(private val orderRepository: OrderRepository) {

    suspend fun getOrderDetail(orderId: Int): OrderDetailStateItem = withContext(MyDispatchers.IO) {
        orderToStateItem(orderRepository.getOrderDetails(orderId))
    }

    private fun orderToStateItem(orderDetails: OrderDetail): OrderDetailStateItem {
        return OrderDetailStateItem(
            orderDetails.id,
            orderDetails.products.map { orderedProductToStateItem(it) },
            String.format(
                "$%s",
                getNumberHumanFormatted(calculateTotalPrice(orderDetails.products))
            )
        )
    }

    private fun calculateTotalPrice(products: List<OrderedProduct>): Int {
        var price = 0
        products.forEach { price += (it.sellingPrice * it.amount) }
        return price
    }

    private fun orderedProductToStateItem(orderedProduct: OrderedProduct): OrderedProductSateItem {
        return OrderedProductSateItem(
            orderedProduct.id,
            orderedProduct.name,
            "Qty: ${orderedProduct.amount}",
            String.format("Price: $%s", getNumberHumanFormatted(orderedProduct.sellingPrice)),
            orderedProduct.imageUrl
        )
    }

    private fun getNumberHumanFormatted(number: Int): String {
        return NumberFormat.getNumberInstance(Locale.US).format(number)
    }
}