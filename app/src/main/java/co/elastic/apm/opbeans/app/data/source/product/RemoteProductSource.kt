package co.elastic.apm.opbeans.app.data.source.product

import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.data.models.ProductDetail
import co.elastic.apm.opbeans.app.data.remote.OpBeansService
import co.elastic.apm.opbeans.app.data.remote.models.RemoteProduct
import co.elastic.apm.opbeans.app.data.remote.models.RemoteProductDetail
import co.elastic.apm.opbeans.app.data.source.product.helpers.ImageUrlBuilder
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

@Singleton
class RemoteProductSource @Inject constructor(private val opBeansService: OpBeansService) {

    suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
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