package co.elastic.apm.opbeans.modules.customers.ui

import co.elastic.apm.opbeans.app.data.models.Customer

sealed class CustomerState {
    object Loading : CustomerState()
    class FinishedLoading(val customers: Customer) : CustomerState()
    class ErrorLoading(val exception: Exception) : CustomerState()
}