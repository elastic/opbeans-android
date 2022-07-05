package co.elastic.apm.opbeans.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import co.elastic.apm.opbeans.app.data.local.entities.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {

    @Insert
    suspend fun insert(cartItemEntity: CartItemEntity): Long

    @Query("SELECT * FROM cart_item ORDER BY created_at DESC")
    fun getAll(): Flow<List<CartItemEntity>>
}