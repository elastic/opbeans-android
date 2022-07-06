package co.elastic.apm.opbeans.app.data.models

data class ProductDetail(
    val id: Int,
    val sku: String,
    val name: String,
    val description: String,
    val type: String,
    val imageUrl: String
)