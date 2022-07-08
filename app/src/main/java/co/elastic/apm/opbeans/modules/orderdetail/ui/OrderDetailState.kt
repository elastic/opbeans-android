package co.elastic.apm.opbeans.modules.orderdetail.ui

import co.elastic.apm.opbeans.modules.orderdetail.data.OrderDetailStateItem

sealed class OrderDetailState {
    object Loading : OrderDetailState()
    class FinishedLoading(val value: OrderDetailStateItem) : OrderDetailState()
    class ErrorLoading(val e: Throwable) : OrderDetailState()
}