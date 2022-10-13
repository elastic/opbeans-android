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
package co.elastic.apm.opbeans.modules.productdetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.CartItemRepository
import co.elastic.apm.opbeans.app.data.repository.ProductRepository
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartItemRepository: CartItemRepository
) : ViewModel() {

    fun fetchProduct(productId: Int, callback: (ProductDetailLoadState) -> Unit) {
        callback.invoke(ProductDetailLoadState.Loading)
        productRepository.getProductById(productId) { result ->
            if (result.isSuccess) {
                callback.invoke(ProductDetailLoadState.FinishedLoading(result.getOrThrow()))
            } else {
                callback.invoke(ProductDetailLoadState.ErrorLoading(result.exceptionOrNull()!!))
            }
        }
    }

    fun addProductToCart(productId: Int, callback: () -> Unit) {
        viewModelScope.launch(MyDispatchers.Main) {
            cartItemRepository.addOrUpdateItem(productId)
            callback.invoke()
        }
    }
}