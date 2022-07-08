package co.elastic.apm.opbeans.modules.account.state

import co.elastic.apm.opbeans.app.data.models.Order

sealed class AccountListState {
    object LoadingList : AccountListState()
    class FinishedLoadingList(val orders: List<Order>) : AccountListState()
    class ErrorLoadingList(val e: Throwable) : AccountListState()
}