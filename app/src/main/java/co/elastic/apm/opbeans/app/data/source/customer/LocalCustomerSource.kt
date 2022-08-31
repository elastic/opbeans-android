package co.elastic.apm.opbeans.app.data.source.customer

import co.elastic.apm.opbeans.app.data.local.AppDatabase
import co.elastic.apm.opbeans.app.data.local.entities.CustomerEntity
import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.data.source.customer.tools.LocationBuilder
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCustomerSource @Inject constructor(appDatabase: AppDatabase) {

    private val customerDao by lazy { appDatabase.customerDao() }

    suspend fun getSetOfCustomers(offset: Int, amount: Int): List<Customer> =
        withContext(MyDispatchers.IO) {
            customerDao.getSetOfCustomers(offset, amount).map { entityToCustomer(it) }
        }

    suspend fun saveAll(customers: List<Customer>) = withContext(MyDispatchers.IO) {
        customerDao.insertAll(customers.map { customerToEntity(it) })
    }

    private fun entityToCustomer(customerEntity: CustomerEntity): Customer {
        return Customer(
            customerEntity.id,
            customerEntity.fullName,
            customerEntity.companyName,
            customerEntity.email,
            customerEntity.city,
            customerEntity.country,
            LocationBuilder.build(customerEntity.city, customerEntity.country)
        )
    }

    private fun customerToEntity(customer: Customer): CustomerEntity {
        return CustomerEntity(
            customer.id,
            customer.fullName,
            customer.companyName,
            customer.email,
            customer.city,
            customer.country
        )
    }

    suspend fun getAmountOfCustomers(): Int {
        return customerDao.getCustomerRowCount()
    }
}