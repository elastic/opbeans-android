package co.elastic.apm.opbeans.modules.orderdetail.data.usecases

import co.elastic.apm.opbeans.app.data.models.OrderDetail
import co.elastic.apm.opbeans.app.data.models.OrderedProduct
import co.elastic.apm.opbeans.app.data.repository.OrderRepository
import co.elastic.apm.opbeans.modules.orderdetail.data.OrderDetailStateItem
import co.elastic.apm.opbeans.modules.orderdetail.data.OrderedProductSateItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderDetailStateUseCase(private val orderRepository: OrderRepository) {

    suspend fun getOrderDetail(orderId: Int): OrderDetailStateItem = withContext(Dispatchers.IO) {
        orderToStateItem(orderRepository.getOrderDetails(orderId))
    }

    private fun orderToStateItem(orderDetails: OrderDetail): OrderDetailStateItem {
        return OrderDetailStateItem(
            orderDetails.id,
            orderDetails.products.map { orderedProductToStateItem(it) },
            String.format("$%d", calculateTotalPrice(orderDetails.products))
        )
    }

    private fun calculateTotalPrice(products: List<OrderedProduct>): Int {
        var price = 0
        products.forEach { price += it.sellingPrice }
        return price
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