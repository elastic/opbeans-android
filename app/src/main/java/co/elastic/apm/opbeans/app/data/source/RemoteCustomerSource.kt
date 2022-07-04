package co.elastic.apm.opbeans.app.data.source

import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.data.remote.OpBeansService
import co.elastic.apm.opbeans.app.data.remote.models.RemoteCustomer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteCustomerSource @Inject constructor(private val opBeansService: OpBeansService) {

    suspend fun getCustomers(): List<Customer> = withContext(Dispatchers.IO) {
        opBeansService.getCustomers().map {
            remoteToCustomer(it)
        }
    }

    private fun remoteToCustomer(remoteCustomer: RemoteCustomer): Customer {
        return Customer(
            remoteCustomer.id,
            remoteCustomer.fullName,
            remoteCustomer.companyName,
            remoteCustomer.email,
            "${remoteCustomer.city}, ${remoteCustomer.country}"
        )
    }
}