package co.elastic.apm.opbeans.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.elastic.apm.opbeans.app.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveAll(products: List<ProductEntity>)

    @Query("SELECT * FROM product ORDER BY id")
    fun getAll(): Flow<List<ProductEntity>>
}