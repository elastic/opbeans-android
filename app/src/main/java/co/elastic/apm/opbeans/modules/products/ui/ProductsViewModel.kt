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
package co.elastic.apm.opbeans.modules.products.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.ProductRepository
import co.elastic.apm.opbeans.modules.products.ui.state.NetworkRequestState
import co.elastic.apm.opbeans.modules.products.ui.state.ProductsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private val internalNetworkRequestState =
        MutableStateFlow<NetworkRequestState>(NetworkRequestState.Running)
    val networkRequestState: StateFlow<NetworkRequestState> =
        internalNetworkRequestState.asStateFlow()
    val products: StateFlow<ProductsState> = productRepository.getProducts()
        .catch { e ->
            ProductsState.Error(e)
        }.map {
            ProductsState.ProductsLoaded(it)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductsState.Loading)

    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                internalNetworkRequestState.update { NetworkRequestState.Running }
                productRepository.fetchRemoteProducts()
                internalNetworkRequestState.update { NetworkRequestState.Successful }
            } catch (e: Exception) {
                internalNetworkRequestState.update { NetworkRequestState.Failed(e) }
            }
        }
    }
}