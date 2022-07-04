package co.elastic.apm.opbeans.app.data.repository

import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.data.source.RemoteCustomerSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(private val remoteCustomerSource: RemoteCustomerSource) {

    suspend fun getCustomers(): List<Customer> {
        return remoteCustomerSource.getCustomers()
    }
}