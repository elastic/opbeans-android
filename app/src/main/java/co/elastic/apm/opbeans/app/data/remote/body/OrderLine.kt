package co.elastic.apm.opbeans.app.data.remote.body

import com.google.gson.annotations.SerializedName

data class OrderLine(
    @SerializedName("id")
    val productId: Int,
    val amount: Int
)
