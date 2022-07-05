package co.elastic.apm.opbeans.app.data.local

import androidx.room.Database
import co.elastic.apm.opbeans.app.data.local.dao.CartItemDao
import co.elastic.apm.opbeans.app.data.local.entities.CartItemEntity

@Database(entities = [CartItemEntity::class], version = 1)
abstract class AppDatabase {
    abstract fun cartItemDao(): CartItemDao
}