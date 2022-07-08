package co.elastic.apm.opbeans.modules.account.state

import co.elastic.apm.opbeans.modules.account.data.AccountStateScreenItem

sealed class AccountState {
    object LoadingScreen : AccountState()
    class FinishedLoadingScreen(val data: AccountStateScreenItem) : AccountState()
    class ErrorLoadingScreen(val e: Throwable) : AccountState()
}