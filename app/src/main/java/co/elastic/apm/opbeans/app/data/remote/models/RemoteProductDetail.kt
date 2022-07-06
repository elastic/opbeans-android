package co.elastic.apm.opbeans.app.data.remote.models

import com.google.gson.annotations.SerializedName

data class RemoteProductDetail(
    val id: Int,
    val sku: String,
    val name: String,
    val description: String,
    val stock: Int,
    val cost: Int,
    @SerializedName("selling_price")
    val sellingPrice: Int,
    @SerializedName("type_name")
    val typeName: String
)