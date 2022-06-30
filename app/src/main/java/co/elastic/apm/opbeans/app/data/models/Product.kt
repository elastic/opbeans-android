package co.elastic.apm.opbeans.app.data.models

data class Product(
    val id: Int,
    val sku: String,
    val name: String,
    val stock: Int,
    val type: String
)
