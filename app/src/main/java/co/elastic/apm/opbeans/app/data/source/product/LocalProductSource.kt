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
package co.elastic.apm.opbeans.app.data.source.product

import co.elastic.apm.opbeans.app.data.local.AppDatabase
import co.elastic.apm.opbeans.app.data.local.entities.ProductEntity
import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.source.product.helpers.ProductEntityMapper.toProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalProductSource @Inject constructor(private val appDatabase: AppDatabase) {

    private val productDao by lazy { appDatabase.productDao() }

    fun getProducts(): Flow<List<Product>> {
        return productDao.getAll().map {
            localListToProducts(it)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun storeProducts(products: List<Product>) {
        val entities = products.map { productToEntity(it) }
        productDao.saveAll(entities)
    }

    private fun localListToProducts(localList: List<ProductEntity>): List<Product> {
        return localList.map { it.toProduct() }
    }

    private fun productToEntity(product: Product): ProductEntity {
        return ProductEntity(
            product.id,
            product.sku,
            product.name,
            product.stock,
            product.type
        )
    }
}