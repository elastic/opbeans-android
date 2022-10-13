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
package co.elastic.apm.opbeans.app.auth

import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.data.repository.CustomerRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class AuthManager @Inject constructor(private val customerRepository: CustomerRepository) {

    private var userId: Customer? = null

    suspend fun getUser(): Customer {
        if (userId == null) {
            setUpUser()
        }
        return userId!!
    }

    private suspend fun setUpUser() {
        val numUsers = getAmountOfCustomers()
        val userOffset = Random(System.currentTimeMillis()).nextInt(0, numUsers - 1)
        val users = customerRepository.getSetOfCustomers(userOffset, 1)
        userId = users.first()
    }

    private suspend fun getAmountOfCustomers(): Int {
        val existingCustomersCount = customerRepository.getAmountOfCustomers()
        return if (existingCustomersCount == 0) {
            customerRepository.fetchCustomers()
            customerRepository.getAmountOfCustomers()
        } else {
            existingCustomersCount
        }
    }
}