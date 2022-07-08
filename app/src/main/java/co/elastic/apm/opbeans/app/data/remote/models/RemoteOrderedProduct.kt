package co.elastic.apm.opbeans.app.data.remote.models

import com.google.gson.annotations.SerializedName

data class RemoteOrderedProduct(
    val amount: Int,
    val id: Int,
    val sku: String,
    val name: String,
    val description: String,
    val stock: Int,
    @SerializedName("selling_price")
    val sellingPrice: Int
)