package co.elastic.apm.opbeans.modules.account.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.data.repository.CustomerRepository
import co.elastic.apm.opbeans.app.data.repository.OrderRepository
import co.elastic.apm.opbeans.modules.account.data.AccountStateScreenItem
import co.elastic.apm.opbeans.modules.account.state.AccountListState
import co.elastic.apm.opbeans.modules.account.state.AccountState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val customerRepository: CustomerRepository,
    private val orderRepository: OrderRepository
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

    private val internalListState = MutableStateFlow<AccountListState>(AccountListState.LoadingList)
    val orders = internalListState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AccountListState.LoadingList)

    fun fetchScreen() {
        viewModelScope.launch {
            try {
                internalState.update { AccountState.LoadingScreen }
                ensureOrdersLoaded()
                val numUsers = getAmountOfCustomers()
                val userOffset = Random.nextInt(0, numUsers - 1)
                val users = customerRepository.getSetOfCustomers(userOffset, 1)
                internalState.update {
                    AccountState.FinishedLoadingScreen(
                        AccountStateScreenItem(users.first())
                    )
                }
            } catch (e: Throwable) {
                internalState.update { AccountState.ErrorLoadingScreen(e) }
            }
        }
    }

    fun fetchOrders() {
        user?.let { user ->
            doFetchOrders(user)
        } ?: throw IllegalStateException("No user is set")
    }

    private fun doFetchOrders(user: Customer) {
        viewModelScope.launch {
            try {
                internalListState.update { AccountListState.LoadingList }
                val orders = orderRepository.getCustomerOrders(user.id)
                internalListState.update { AccountListState.FinishedLoadingList(orders) }
            } catch (e: Throwable) {
                internalListState.update { AccountListState.ErrorLoadingList(e) }
            }
        }
    }

    private suspend fun getAmountOfCustomers(): Int {
        val existingCustomersCount = customerRepository.getAmountOfCustomers()
        return if (existingCustomersCount == 0) {
            customerRepository.fetchCustomers()
            customerRepository.getAmountOfCustomers()
        } else {
            existingCustomersCount
        }
    }

    private suspend fun ensureOrdersLoaded() {
        val existingOrdersCount = orderRepository.getTotalAmountOfOrders()
        if (existingOrdersCount == 0) {
            orderRepository.fetchOrders()
        }
    }
}