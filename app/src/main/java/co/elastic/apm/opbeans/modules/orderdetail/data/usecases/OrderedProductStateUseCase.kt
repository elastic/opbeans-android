package co.elastic.apm.opbeans.modules.orderdetail.data.usecases

import co.elastic.apm.opbeans.app.data.models.OrderDetail
import co.elastic.apm.opbeans.app.data.models.OrderedProduct
import co.elastic.apm.opbeans.app.data.repository.OrderRepository
import co.elastic.apm.opbeans.modules.orderdetail.data.OrderDetailStateItem
import co.elastic.apm.opbeans.modules.orderdetail.data.OrderedProductSateItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderedProductStateUseCase(private val orderRepository: OrderRepository) {

    suspend fun getOrderDetail(orderId: Int): OrderDetailStateItem = withContext(Dispatchers.IO) {
        orderToStateItem(orderRepository.getOrderDetails(orderId))
    }

    private fun orderToStateItem(orderDetails: OrderDetail): OrderDetailStateItem {
        return OrderDetailStateItem(
            orderDetails.id,
            orderDetails.products.map { orderedProductToStateItem(it) }
        )
    }

    private fun orderedProductToStateItem(orderedProduct: OrderedProduct): OrderedProductSateItem {
        return OrderedProductSateItem(
            orderedProduct.id,
            orderedProduct.name,
            "Qty: ${orderedProduct.amount}",
            String.format("Price: $%d", orderedProduct.sellingPrice),
            orderedProduct.imageUrl
        )
    }
}