package co.elastic.apm.opbeans.app.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import co.elastic.apm.opbeans.app.data.local.entities.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders ORDER BY created_at DESC")
    fun getOrders(): Flow<List<OrderEntity>>
}