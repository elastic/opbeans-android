package co.elastic.apm.opbeans.modules.account.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.elastic.apm.opbeans.app.auth.AuthManager
import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.data.repository.OrderRepository
import co.elastic.apm.opbeans.modules.account.data.AccountStateScreenItem
import co.elastic.apm.opbeans.modules.account.data.paging.AccountOrdersPagingSource
import co.elastic.apm.opbeans.modules.account.state.AccountState
import co.elastic.apm.opbeans.modules.orders.data.cases.OrderStateItemCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authManager: AuthManager,
    private val orderRepository: OrderRepository,
    private val orderStateItemCase: OrderStateItemCase
) : ViewModel() {

    private var user: Customer? = null

    private val internalState = MutableStateFlow<AccountState>(AccountState.LoadingScreen)
    val state = internalState.asStateFlow()
        .onEach {
            if (it is AccountState.FinishedLoadingScreen) {
                user = it.data.customer
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AccountState.LoadingScreen)

    val orders =
        Pager(PagingConfig(pageSize = 10, initialLoadSize = 10, enablePlaceholders = false)) {
            AccountOrdersPagingSource(orderStateItemCase, user!!.id)
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    fun fetchScreen() {
        viewModelScope.launch {
            try {
                internalState.update { AccountState.LoadingScreen }
                ensureOrdersLoaded()
                internalState.update {
                    AccountState.FinishedLoadingScreen(
                        AccountStateScreenItem(authManager.getUser())
                    )
                }
            } catch (e: Throwable) {
                internalState.update { AccountState.ErrorLoadingScreen(e) }
            }
        }
    }

    private suspend fun ensureOrdersLoaded() {
        val existingOrdersCount = orderRepository.getTotalAmountOfOrders()
        if (existingOrdersCount == 0) {
            orderRepository.fetchOrders()
        }
    }
}