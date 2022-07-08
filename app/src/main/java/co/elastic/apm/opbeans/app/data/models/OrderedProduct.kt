package co.elastic.apm.opbeans.app.data.models

data class OrderedProduct(
    val amount: Int,
    val id: Int,
    val sku: String,
    val name: String,
    val description: String,
    val stock: Int,
    val sellingPrice: Int,
    val imageUrl: String
)