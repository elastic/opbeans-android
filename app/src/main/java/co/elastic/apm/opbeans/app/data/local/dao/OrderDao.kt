package co.elastic.apm.opbeans.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.elastic.apm.opbeans.app.data.local.entities.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders ORDER BY created_at DESC")
    fun getOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(orders: List<OrderEntity>)

    @Insert
    suspend fun insert(order: OrderEntity)

    @Query("SELECT * FROM orders ORDER BY created_at DESC LIMIT :offset, :amount")
    suspend fun getSetOfOrders(offset: Int, amount: Int): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE customer_id = :customerId ORDER BY created_at DESC")
    suspend fun getCustomerOrders(customerId: Int): List<OrderEntity>

    @Query("SELECT COUNT(id) from orders")
    suspend fun getOrderRowCount(): Int
}