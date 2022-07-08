package co.elastic.apm.opbeans.modules.orderdetail.ui

import androidx.lifecycle.ViewModel
import co.elastic.apm.opbeans.modules.orderdetail.data.usecases.OrderDetailStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(private val orderDetailStateUseCase: OrderDetailStateUseCase) : ViewModel() {


}