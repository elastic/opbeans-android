package co.elastic.apm.opbeans.app.data.source.cart

import co.elastic.apm.opbeans.app.data.local.AppDatabase
import co.elastic.apm.opbeans.app.data.local.entities.CartItemEntity
import co.elastic.apm.opbeans.app.data.local.relationships.CartItemAndProduct
import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.source.cart.exceptions.ProductAlreadyInCartException
import co.elastic.apm.opbeans.app.data.source.product.helpers.ProductEntityMapper.toProduct
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class LocalCartItemSource @Inject constructor(private val appDatabase: AppDatabase) {

    private val cartItemDao by lazy { appDatabase.cartItemDao() }

    fun getAllCartItems(): Flow<List<CartItem>> {
        return cartItemDao.getAllWithProducts().map { list ->
            list.map { entityToCartItem(it) }
        }
    }

    /**
     * @throws [ProductAlreadyInCartException] if the product is already added to the cart.
     */
    suspend fun addItem(productId: Int) {
        verifyProductNotInCart(productId)
        cartItemDao.insert(CartItemEntity(0, productId, 1, Date().time))
    }

    private suspend fun verifyProductNotInCart(productId: Int) {
        val existingCartItemForProduct = cartItemDao.getCartItemWithProduct(productId)
        if (existingCartItemForProduct != null) {
            throw ProductAlreadyInCartException(productId)
        }
    }

    private fun entityToCartItem(cartItem: CartItemAndProduct): CartItem {
        return CartItem(
            cartItem.productEntity.toProduct(),
            cartItem.cartItemEntity.amount
        )
    }
}