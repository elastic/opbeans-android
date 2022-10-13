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

import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.models.ProductDetail
import co.elastic.apm.opbeans.app.data.remote.OpBeansService
import co.elastic.apm.opbeans.app.data.remote.models.RemoteProduct
import co.elastic.apm.opbeans.app.data.remote.models.RemoteProductDetail
import co.elastic.apm.opbeans.app.data.source.product.helpers.ImageUrlBuilder
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteProductSource @Inject constructor(private val opBeansService: OpBeansService) {

    suspend fun getProducts(): List<Product> = withContext(MyDispatchers.IO) {
        opBeansService.getProducts().map {
            remoteToProduct(it)
        }
    }

    fun getProductById(id: Int, callback: (Result<ProductDetail>) -> Unit) {
        opBeansService.getProductById(id).enqueue(object : Callback<RemoteProductDetail> {
            override fun onResponse(
                call: Call<RemoteProductDetail>,
                response: Response<RemoteProductDetail>
            ) {
                if (response.isSuccessful) {
                    callback.invoke(Result.success(remoteToProductDetail(response.body()!!)))
                } else {
                    callback.invoke(Result.failure(HttpException(response)))
                }
            }

            override fun onFailure(call: Call<RemoteProductDetail>, t: Throwable) {
                callback.invoke(Result.failure(t))
            }
        })
    }

    private fun remoteToProduct(remoteProduct: RemoteProduct): Product {
        return Product(
            remoteProduct.id,
            remoteProduct.sku,
            remoteProduct.name,
            remoteProduct.stock,
            remoteProduct.typeName,
            ImageUrlBuilder.build(remoteProduct.sku)
        )
    }

    private fun remoteToProductDetail(remoteProduct: RemoteProductDetail): ProductDetail {
        return ProductDetail(
            remoteProduct.id,
            remoteProduct.sku,
            remoteProduct.name,
            remoteProduct.description,
            remoteProduct.typeName,
            remoteProduct.stock,
            remoteProduct.sellingPrice,
            ImageUrlBuilder.build(remoteProduct.sku)
        )
    }
}