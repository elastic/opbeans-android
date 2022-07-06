package co.elastic.apm.opbeans.app.data.local.relationships

import androidx.room.Embedded
import androidx.room.Relation
import co.elastic.apm.opbeans.app.data.local.entities.CartItemEntity
import co.elastic.apm.opbeans.app.data.local.entities.ProductEntity

data class CartItemAndProduct(
    @Embedded val cartItemEntity: CartItemEntity,
    @Relation(
        parentColumn = "product_id",
        entityColumn = "id"
    )
    val productEntity: ProductEntity
)