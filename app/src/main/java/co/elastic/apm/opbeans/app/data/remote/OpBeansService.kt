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
package co.elastic.apm.opbeans.app.data.remote

import co.elastic.apm.opbeans.app.data.remote.body.CreateOrderBody
import co.elastic.apm.opbeans.app.data.remote.models.CreateOrderResponse
import co.elastic.apm.opbeans.app.data.remote.models.RemoteCustomer
import co.elastic.apm.opbeans.app.data.remote.models.RemoteOrder
import co.elastic.apm.opbeans.app.data.remote.models.RemoteOrderDetail
import co.elastic.apm.opbeans.app.data.remote.models.RemoteProduct
import co.elastic.apm.opbeans.app.data.remote.models.RemoteProductDetail
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OpBeansService {

    @GET("products")
    suspend fun getProducts(): List<RemoteProduct>

    @GET("customers")
    suspend fun getCustomers(): List<RemoteCustomer>

    @GET("orders")
    suspend fun getOrders(): List<RemoteOrder>

    @GET("products/{product_id}")
    fun getProductById(@Path("product_id") id: Int): Call<RemoteProductDetail>

    @POST("orders")
    suspend fun createOrder(@Body body: CreateOrderBody): CreateOrderResponse

    @GET("orders/{order_id}")
    suspend fun getOrderById(@Path("order_id") id: Int): RemoteOrderDetail
}