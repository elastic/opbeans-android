package co.elastic.apm.opbeans.app.data.remote.models

import com.google.gson.annotations.SerializedName

data class RemoteCustomer(
    val id: Int,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("company_name")
    val companyName: String,
    val email: String,
    val city: String,
    val country: String
)