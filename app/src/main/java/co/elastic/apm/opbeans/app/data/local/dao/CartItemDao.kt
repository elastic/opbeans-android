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
package co.elastic.apm.opbeans.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import co.elastic.apm.opbeans.app.data.local.entities.CartItemEntity
import co.elastic.apm.opbeans.app.data.local.relationships.CartItemAndProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {

    @Insert
    suspend fun insert(cartItemEntity: CartItemEntity): Long

    @Transaction
    @Query("SELECT * FROM cart_item ORDER BY created_at DESC")
    fun getAllWithProducts(): Flow<List<CartItemAndProduct>>

    @Transaction
    @Query("SELECT * FROM cart_item WHERE product_id = :productId")
    suspend fun getCartItemWithProduct(productId: Int): CartItemAndProduct?

    @Query("DELETE from cart_item")
    suspend fun deleteAll()

    @Update
    suspend fun update(cartItemEntity: CartItemEntity)
}