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

import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.data.source.customer.LocalCustomerSource
import co.elastic.apm.opbeans.app.data.source.customer.RemoteCustomerSource
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val remoteCustomerSource: RemoteCustomerSource,
    private val localCustomerSource: LocalCustomerSource
) {

    suspend fun getSetOfCustomers(offset: Int, amount: Int): List<Customer> =
        withContext(MyDispatchers.IO) {
            localCustomerSource.getSetOfCustomers(offset, amount)
        }

    suspend fun fetchCustomers() = withContext(MyDispatchers.IO) {
        val remoteCustomers = remoteCustomerSource.getCustomers()
        localCustomerSource.saveAll(remoteCustomers)
    }

    suspend fun getAmountOfCustomers(): Int {
        return localCustomerSource.getAmountOfCustomers()
    }
}