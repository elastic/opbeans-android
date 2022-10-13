/* 
Licensed to Elasticsearch B.V. under one or more contributor
license agreements. See the NOTICE file distributed with
this work for additional information regarding copyright
ownership. Elasticsearch B.V. licenses this file to you under
the Apache License, Version 2.0 (the "License"); you may
not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License. 
*/
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