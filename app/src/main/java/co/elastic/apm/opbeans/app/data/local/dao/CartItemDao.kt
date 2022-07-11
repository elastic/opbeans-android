package co.elastic.apm.opbeans.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import co.elastic.apm.opbeans.app.data.local.entities.CartItemEntity
import co.elastic.apm.opbeans.app.data.local.relationships.CartItemAndProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {

    @Insert
    suspend fun insert(cartItemEntity: CartItemEntity): Long

    @Transaction
    @Query("SELECT * FROM cart_item ORDER BY created_at DESC")
    fun getAllWithProducts(): Flow<List<CartItemAndProduct>>

    @Transaction
    @Query("SELECT * FROM cart_item WHERE product_id = :productId")
    suspend fun getCartItemWithProduct(productId: Int): CartItemAndProduct?

    @Query("DELETE from cart_item")
    suspend fun deleteAll()

    @Update
    suspend fun update(cartItemEntity: CartItemEntity)
}