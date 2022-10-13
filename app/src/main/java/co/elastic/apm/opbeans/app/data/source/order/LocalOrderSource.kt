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

import co.elastic.apm.opbeans.app.data.local.AppDatabase
import co.elastic.apm.opbeans.app.data.local.entities.OrderEntity
import co.elastic.apm.opbeans.app.data.models.Order
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalOrderSource @Inject constructor(private val appDatabase: AppDatabase) {

    private val orderDao by lazy { appDatabase.orderDao() }

    suspend fun getSetOfOrders(offset: Int, amount: Int): List<Order> =
        withContext(MyDispatchers.IO) {
            orderDao.getSetOfOrders(offset, amount).map { entityToOrder(it) }
        }

    suspend fun saveOrder(order: Order) = withContext(MyDispatchers.IO) {
        orderDao.insert(orderToEntity(order))
    }

    suspend fun saveAll(orders: List<Order>) = withContext(MyDispatchers.IO) {
        orderDao.insertAll(orders.map { order -> orderToEntity(order) })
    }

    fun getAllCustomerOrders(customerId: Int): Flow<List<Order>> {
        return orderDao.getAllCustomerOrders(customerId)
            .map { list -> list.map { entityToOrder(it) } }
            .flowOn(Dispatchers.IO)
    }

    private fun orderToEntity(order: Order): OrderEntity {
        return OrderEntity(
            order.id,
            order.customerId,
            order.customerName,
            order.createdAt.time
        )
    }

    private fun entityToOrder(orderEntity: OrderEntity): Order {
        return Order(
            orderEntity.id,
            orderEntity.customerId,
            orderEntity.customerName,
            Date(orderEntity.createdAt)
        )
    }

    suspend fun getTotalAmountOfOrders(): Int {
        return orderDao.getOrderRowCount()
    }
}