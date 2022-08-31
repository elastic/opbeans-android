package co.elastic.apm.opbeans.modules.orders.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.elastic.apm.opbeans.app.data.repository.OrderRepository
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import co.elastic.apm.opbeans.modules.orders.data.cases.OrderStateItemCase
import co.elastic.apm.opbeans.modules.orders.data.paging.OrdersPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    orderStateItemCase: OrderStateItemCase,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val internalState: MutableStateFlow<OrdersNetworkState> =
        MutableStateFlow(OrdersNetworkState.Loading)
    val state = internalState.asStateFlow()
    val orders =
        Pager(PagingConfig(pageSize = 10, initialLoadSize = 10, enablePlaceholders = false)) {
            OrdersPagingSource(orderStateItemCase)
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    fun fetchOrders() {
        viewModelScope.launch(MyDispatchers.Main) {
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