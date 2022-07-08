package co.elastic.apm.opbeans.modules.orders.ui

sealed class OrdersNetworkState {
    object Loading : OrdersNetworkState()
    object FinishedLoading : OrdersNetworkState()
    class ErrorLoading(val exception: Exception) : OrdersNetworkState()
}