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