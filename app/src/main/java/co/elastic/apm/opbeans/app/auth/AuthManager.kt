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