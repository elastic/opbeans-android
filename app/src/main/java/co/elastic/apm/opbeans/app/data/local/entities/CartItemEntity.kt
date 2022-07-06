package co.elastic.apm.opbeans.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "cart_item", indices = [Index(value = ["product_id"], unique = true)])
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "product_id") val productId: Int,
    val amount: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: Long
)