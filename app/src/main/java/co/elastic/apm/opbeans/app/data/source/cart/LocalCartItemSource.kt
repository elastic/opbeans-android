/* 
Licensed to Elasticsearch B.V. under one or more contributor
license agreements. See the NOTICE file distributed with
this work for additional information regarding copyright
ownership. Elasticsearch B.V. licenses this file to you under
the Apache License, Version 2.0 (the "License"); you may
not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License. 
*/
package co.elastic.apm.opbeans.app.data.source.cart

import co.elastic.apm.opbeans.app.data.local.AppDatabase
import co.elastic.apm.opbeans.app.data.local.entities.CartItemEntity
import co.elastic.apm.opbeans.app.data.local.relationships.CartItemAndProduct
import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.source.product.helpers.ProductEntityMapper.toProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCartItemSource @Inject constructor(private val appDatabase: AppDatabase) {

    private val cartItemDao by lazy { appDatabase.cartItemDao() }

    fun getAllCartItems(): Flow<List<CartItem>> {
        return cartItemDao.getAllWithProducts().map { list ->
            list.map { entityToCartItem(it) }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addOrUpdateItem(productId: Int) {
        val existingCartItemForProduct = cartItemDao.getCartItemWithProduct(productId)
        if (existingCartItemForProduct != null) {
            val newAmount = existingCartItemForProduct.cartItemEntity.amount + 1
            cartItemDao.update(existingCartItemForProduct.cartItemEntity.copy(amount = newAmount))
        } else {
            cartItemDao.insert(CartItemEntity(0, productId, 1, Date().time))
        }
    }

    suspend fun deleteAll() {
        cartItemDao.deleteAll()
    }

    private fun entityToCartItem(cartItem: CartItemAndProduct): CartItem {
        return CartItem(
            cartItem.productEntity.toProduct(),
            cartItem.cartItemEntity.amount
        )
    }
}