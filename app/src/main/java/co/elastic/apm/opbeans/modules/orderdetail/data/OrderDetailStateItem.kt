package co.elastic.apm.opbeans.modules.orderdetail.data

data class OrderDetailStateItem(
    val id: Int,
    val products: List<OrderedProductSateItem>
)