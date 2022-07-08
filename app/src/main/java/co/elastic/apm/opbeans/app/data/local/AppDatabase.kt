package co.elastic.apm.opbeans.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import co.elastic.apm.opbeans.app.data.local.dao.CartItemDao
import co.elastic.apm.opbeans.app.data.local.dao.OrderDao
import co.elastic.apm.opbeans.app.data.local.dao.ProductDao
import co.elastic.apm.opbeans.app.data.local.entities.CartItemEntity
import co.elastic.apm.opbeans.app.data.local.entities.OrderEntity
import co.elastic.apm.opbeans.app.data.local.entities.ProductEntity

@Database(
    entities = [CartItemEntity::class, ProductEntity::class, OrderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartItemDao(): CartItemDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
}