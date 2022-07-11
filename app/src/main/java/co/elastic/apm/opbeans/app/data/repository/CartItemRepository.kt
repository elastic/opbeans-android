package co.elastic.apm.opbeans.app.data.repository

import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.source.cart.LocalCartItemSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class CartItemRepository @Inject constructor(private val localCartItemSource: LocalCartItemSource) {

    fun getAllCartItems(): Flow<List<CartItem>> {
        return localCartItemSource.getAllCartItems()
    }

    suspend fun addOrUpdateItem(productId: Int) {
        localCartItemSource.addOrUpdateItem(productId)
    }

    suspend fun deleteAll() {
        localCartItemSource.deleteAll()
    }
}