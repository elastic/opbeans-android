package co.elastic.apm.opbeans.modules.orderdetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import co.elastic.apm.opbeans.modules.orderdetail.data.usecases.OrderDetailStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(private val orderDetailStateUseCase: OrderDetailStateUseCase) :
    ViewModel() {

    private val internalState = MutableStateFlow<OrderDetailState>(OrderDetailState.Loading)
    val state = internalState.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OrderDetailState.Loading)

    fun fetchOrderDetail(orderId: Int) {
        viewModelScope.launch(MyDispatchers.Main) {
            try {
                internalState.update { OrderDetailState.Loading }
                val order = orderDetailStateUseCase.getOrderDetail(orderId)
                internalState.update { OrderDetailState.FinishedLoading(order) }
            } catch (e: Throwable) {
                internalState.update { OrderDetailState.ErrorLoading(e) }
            }
        }
    }
}