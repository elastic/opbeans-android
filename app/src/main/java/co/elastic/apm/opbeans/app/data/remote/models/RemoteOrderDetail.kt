package co.elastic.apm.opbeans.app.data.remote.models

import com.google.gson.annotations.SerializedName

data class RemoteOrderDetail(
    val id: Int,
    @SerializedName("customer_id")
    val customerId: Int,
    @SerializedName("lines")
    val products: List<RemoteOrderedProduct>,
    @SerializedName("created_at")
    val createdAt: String
)