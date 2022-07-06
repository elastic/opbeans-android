package co.elastic.apm.opbeans.app.data.remote.models

import com.google.gson.annotations.SerializedName

data class CreateOrderResponse(@SerializedName("id") val orderId: Int)