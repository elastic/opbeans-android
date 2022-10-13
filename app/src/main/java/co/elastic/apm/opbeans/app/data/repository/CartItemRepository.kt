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
package co.elastic.apm.opbeans.app.data.repository

import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.source.cart.LocalCartItemSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

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