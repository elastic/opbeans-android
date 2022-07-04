package co.elastic.apm.opbeans.modules.orders.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.modules.orders.data.cases.OrderStateItemCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OrdersViewModel @Inject constructor(private val orderStateItemCase: OrderStateItemCase) :
    ViewModel() {

    private val internalState: MutableStateFlow<OrdersState> =
        MutableStateFlow(OrdersState.Loading)
    val state = internalState.asStateFlow()

    fun fetchOrders() {
        viewModelScope.launch {
            try {
                internalState.update { OrdersState.Loading }
                val orders = orderStateItemCase.getOrderStateItems()
                internalState.update { OrdersState.FinishedLoading(orders) }
            } catch (e: Exception) {
                internalState.update { OrdersState.ErrorLoading(e) }
            }
        }
    }
}