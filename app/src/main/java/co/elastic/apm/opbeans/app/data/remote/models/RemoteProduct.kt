package co.elastic.apm.opbeans.app.data.remote.models

import com.google.gson.annotations.SerializedName

data class RemoteProduct(
    val id: Int,
    val sku: String,
    val name: String,
    val stock: Int,
    @SerializedName("type_name")
    val typeName: String
)