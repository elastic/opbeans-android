package co.elastic.apm.opbeans.modules.orderdetail.data

data class OrderedProductSateItem(
    val id: Int,
    val name: String,
    val amount: String,
    val price: String,
    val imageUrl: String
)