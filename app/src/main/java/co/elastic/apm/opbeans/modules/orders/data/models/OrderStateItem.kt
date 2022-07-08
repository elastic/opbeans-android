package co.elastic.apm.opbeans.modules.orders.data.models

data class OrderStateItem(
    val id: Int,
    val displayId: String,
    val customerName: String,
    val date: String
)