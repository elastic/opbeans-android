package co.elastic.apm.opbeans.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_item")
data class CartItemEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "product_id") val productId: Int,
    val amount: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)