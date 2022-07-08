package co.elastic.apm.opbeans.app.data.models

import java.util.Date

data class OrderDetail(
    val id: Int,
    val customerId: Int,
    val createdAt: Date,
    val products: List<OrderedProduct>
)