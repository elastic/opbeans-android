package co.elastic.apm.opbeans.modules.customers.ui

import co.elastic.apm.opbeans.app.data.models.Customer

sealed class CustomersState {
    object Loading : CustomersState()
    class FinishedLoading(val customers: List<Customer>) : CustomersState()
    class ErrorLoading(val exception: Exception) : CustomersState()
}