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
package co.elastic.apm.opbeans.app.data.source.order

import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.data.models.Order
import co.elastic.apm.opbeans.app.data.models.OrderDetail
import co.elastic.apm.opbeans.app.data.models.OrderedProduct
import co.elastic.apm.opbeans.app.data.remote.OpBeansService
import co.elastic.apm.opbeans.app.data.remote.body.CreateOrderBody
import co.elastic.apm.opbeans.app.data.remote.body.OrderLine
import co.elastic.apm.opbeans.app.data.remote.models.RemoteOrder
import co.elastic.apm.opbeans.app.data.remote.models.RemoteOrderDetail
import co.elastic.apm.opbeans.app.data.remote.models.RemoteOrderedProduct
import co.elastic.apm.opbeans.app.data.source.product.helpers.ImageUrlBuilder
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteOrderSource @Inject constructor(private val opBeansService: OpBeansService) {

    companion object {
        private val remoteDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
        private val utcDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    }

    suspend fun createOrder(customerId: Int, items: List<CartItem>) = withContext(MyDispatchers.IO) {
        val lines = items.map { OrderLine(it.product.id, it.amount) }
        opBeansService.createOrder(CreateOrderBody(customerId, lines))
    }

    suspend fun getOrders(): List<Order> = withContext(MyDispatchers.IO) {
        opBeansService.getOrders().map {
            remoteToOrder(it)
        }
    }

    suspend fun getOrderDetails(orderId: Int): OrderDetail = withContext(MyDispatchers.IO) {
        remoteToOrderDetail(opBeansService.getOrderById(orderId))
    }

    private fun remoteToOrderDetail(remoteOrder: RemoteOrderDetail): OrderDetail {
        return OrderDetail(
            remoteOrder.id,
            remoteOrder.customerId,
            parseRemoteDate(remoteOrder.createdAt),
            remoteOrder.products.map { remoteToOrderedProduct(it) }
        )
    }

    private fun remoteToOrderedProduct(remoteOrderedProduct: RemoteOrderedProduct): OrderedProduct {
        return OrderedProduct(
            remoteOrderedProduct.amount,
            remoteOrderedProduct.id,
            remoteOrderedProduct.sku,
            remoteOrderedProduct.name,
            remoteOrderedProduct.description,
            remoteOrderedProduct.stock,
            remoteOrderedProduct.sellingPrice,
            ImageUrlBuilder.build(remoteOrderedProduct.sku)
        )
    }

    private fun remoteToOrder(remoteOrder: RemoteOrder): Order {
        return Order(
            remoteOrder.id,
            remoteOrder.customerId,
            remoteOrder.customerName,
            parseRemoteDate(remoteOrder.createdAt)
        )
    }

    private fun parseRemoteDate(remoteDate: String): Date {
        val date = try {
            remoteDateFormat.parse(remoteDate)!!
        } catch (e: ParseException) {
            utcDateFormat.parse(remoteDate)!!
        }
        return date
    }
}