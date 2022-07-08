package co.elastic.apm.opbeans.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import co.elastic.apm.opbeans.app.data.local.entities.CustomerEntity

@Dao
interface CustomerDao {

    @Query("SELECT * FROM customer LIMIT :offset, :amount")
    suspend fun getSetOfCustomers(offset: Int, amount: Int): List<CustomerEntity>

    @Insert
    suspend fun insertAll(customers: List<CustomerEntity>)
}
