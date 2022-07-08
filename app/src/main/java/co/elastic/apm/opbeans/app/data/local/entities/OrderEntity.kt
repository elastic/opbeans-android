package co.elastic.apm.opbeans.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "customer_id")
    val customerId: Int,
    @ColumnInfo(name = "customer_name")
    val customerName: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)