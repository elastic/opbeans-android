package co.elastic.apm.opbeans.app.data.remote.body

import com.google.gson.annotations.SerializedName

data class CreateOrderBody(
    @SerializedName("customer_id")
    val customerId: Int,
    val products: List<OrderLine>
)