package co.elastic.apm.opbeans.modules.orders.ui

import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem

sealed class OrdersState {
    object Loading : OrdersState()
    class FinishedLoading(val orders: List<OrderStateItem>) : OrdersState()
    class ErrorLoading(exception: Exception) : OrdersState()
}