package co.elastic.apm.opbeans.app.data.remote

import co.elastic.apm.opbeans.app.data.remote.models.RemoteProduct
import retrofit2.http.GET

interface OpBeansService {

    @GET("products")
    suspend fun getProducts(): List<RemoteProduct>
}