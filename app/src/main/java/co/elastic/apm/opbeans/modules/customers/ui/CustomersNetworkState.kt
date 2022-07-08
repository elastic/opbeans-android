package co.elastic.apm.opbeans.modules.customers.ui

sealed class CustomersNetworkState {
    object Loading : CustomersNetworkState()
    object FinishedLoading : CustomersNetworkState()
    class ErrorLoading(val exception: Exception) : CustomersNetworkState()
}