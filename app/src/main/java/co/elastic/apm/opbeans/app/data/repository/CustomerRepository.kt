package co.elastic.apm.opbeans.app.data.repository

import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.data.source.customer.LocalCustomerSource
import co.elastic.apm.opbeans.app.data.source.customer.RemoteCustomerSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class CustomerRepository @Inject constructor(
    private val remoteCustomerSource: RemoteCustomerSource,
    private val localCustomerSource: LocalCustomerSource
) {

    suspend fun getSetOfCustomers(offset: Int, amount: Int): List<Customer> =
        withContext(Dispatchers.IO) {
            localCustomerSource.getSetOfCustomers(offset, amount)
        }

    suspend fun fetchCustomers() = withContext(Dispatchers.IO) {
        val remoteCustomers = remoteCustomerSource.getCustomers()
        localCustomerSource.saveAll(remoteCustomers)
    }

    suspend fun getAmountOfCustomers(): Int {
        return localCustomerSource.getAmountOfCustomers()
    }
}