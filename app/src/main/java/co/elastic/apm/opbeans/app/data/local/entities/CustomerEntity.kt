package co.elastic.apm.opbeans.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "customer")
data class CustomerEntity(
    val id: Int,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    @ColumnInfo(name = "company_name")
    val companyName: String,
    val email: String,
    val city: String,
    val country: String
)
