package co.elastic.apm.opbeans.app.data.remote.models

import com.google.gson.annotations.SerializedName

data class RemoteOrder(
    val id: Int,
    @SerializedName("customer_name")
    val customerName: String,
    @SerializedName("created_at")
    val createdAt: String
)