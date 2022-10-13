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
package co.elastic.apm.opbeans.modules.products

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import co.elastic.apm.opbeans.app.ui.ListDivider
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.productdetail.ProductDetailActivity
import co.elastic.apm.opbeans.modules.products.ui.ProductsViewModel
import co.elastic.apm.opbeans.modules.products.ui.products.ProductListAdapter
import co.elastic.apm.opbeans.modules.products.ui.state.NetworkRequestState
import co.elastic.apm.opbeans.modules.products.ui.state.ProductsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {

    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var productList: LoadableList
    private lateinit var productListAdapter: ProductListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initList()

        lifecycleScope.launch(MyDispatchers.Main) {
            viewModel.products.collectLatest {
                when (it) {
                    is ProductsState.ProductsLoaded -> populateProductList(it.products)
                    is ProductsState.Loading -> productList.showLoading()
                    is ProductsState.Error -> productList.showError(it.e)
                }
            }
        }
        lifecycleScope.launch(MyDispatchers.Main) {
            viewModel.networkRequestState.collectLatest {
                when (it) {
                    is NetworkRequestState.Running -> productList.showLoading()
                    is NetworkRequestState.Failed -> onNetworkRequestFailed(it)
                    is NetworkRequestState.Successful -> productList.hideLoading()
                }
            }
        }
    }

    private fun onNetworkRequestFailed(it: NetworkRequestState.Failed) {
        productList.hideLoading()
        showNetworkErrorMessage(it.e)
    }

    private fun showNetworkErrorMessage(e: Throwable) {
        val message = getString(R.string.error_message_products_request, e.message)
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun initList() {
        productListAdapter = ProductListAdapter(::onItemClicked)
        val recyclerView = productList.getList()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(ListDivider(requireContext()))
        recyclerView.adapter = productListAdapter

        productList.onRefreshRequested { viewModel.fetchProducts() }
    }

    private fun onItemClicked(productId: Int, productName: String) {
        ProductDetailActivity.launch(requireContext(), productId, productName)
    }

    private fun initViews(view: View) {
        productList = view.findViewById(R.id.product_list)
    }

    private fun populateProductList(products: List<Product>) {
        productList.showList()
        productListAdapter.submitList(products)
    }
}