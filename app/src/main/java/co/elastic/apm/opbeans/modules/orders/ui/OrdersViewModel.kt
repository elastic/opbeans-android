package co.elastic.apm.opbeans.modules.orders.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.OrderRepository
import co.elastic.apm.opbeans.modules.orders.data.cases.OrderStateItemCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OrdersViewModel @Inject constructor(
    orderStateItemCase: OrderStateItemCase,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val internalState: MutableStateFlow<OrdersNetworkState> =
        MutableStateFlow(OrdersNetworkState.Loading)
    val state = internalState.asStateFlow()
    val orders = orderStateItemCase.orders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun fetchOrders() {
        viewModelScope.launch {
            try {
                internalState.update { OrdersNetworkState.Loading }
                orderRepository.fetchOrders()
                internalState.update { OrdersNetworkState.FinishedLoading }
            } catch (e: Exception) {
                internalState.update { OrdersNetworkState.ErrorLoading(e) }
            }
        }
    }
}