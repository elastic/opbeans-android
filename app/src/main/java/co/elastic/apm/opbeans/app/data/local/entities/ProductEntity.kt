package co.elastic.apm.opbeans.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val sku: String,
    val name: String,
    val stock: Int,
    @ColumnInfo(name = "type_name")
    val typeName: String
)