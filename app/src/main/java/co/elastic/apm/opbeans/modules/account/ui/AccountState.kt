package co.elastic.apm.opbeans.modules.account.ui

import co.elastic.apm.opbeans.modules.account.data.AccountStateScreenItem

sealed class AccountState {
    object LoadingScreen : AccountState()
    class FinishedLoadingScreen(val data: AccountStateScreenItem) : AccountState()
    class ErrorLoadingScreen(val e: Throwable) : AccountState()
    object LoadingList : AccountState()
    object FinishedLoadingList : AccountState()
    class ErrorLoadingList(val e: Throwable) : AccountState()
}