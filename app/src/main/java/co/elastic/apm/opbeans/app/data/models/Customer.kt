package co.elastic.apm.opbeans.app.data.models

data class Customer(
    val id: Int,
    val fullName: String,
    val companyName: String,
    val email: String,
    val location: String
)