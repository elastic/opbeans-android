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