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
package co.elastic.apm.opbeans.modules.customers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import co.elastic.apm.opbeans.app.tools.showToast
import co.elastic.apm.opbeans.app.ui.ListDivider
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.customers.ui.CustomersNetworkState
import co.elastic.apm.opbeans.modules.customers.ui.CustomersViewModel
import co.elastic.apm.opbeans.modules.customers.ui.list.CustomerListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomersFragment : Fragment(R.layout.fragment_customers) {

    private val viewModel: CustomersViewModel by viewModels()
    private lateinit var list: LoadableList
    private lateinit var adapter: CustomerListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initList()

        lifecycleScope.launch(MyDispatchers.Main) {
            viewModel.state.collectLatest {
                when (it) {
                    is CustomersNetworkState.Loading -> list.showLoading()
                    is CustomersNetworkState.ErrorLoading -> onNetworkError(it)
                    is CustomersNetworkState.FinishedLoading -> onNetworkRequestFinished()
                }
            }
        }
        lifecycleScope.launch(MyDispatchers.Main) {
            viewModel.customers.collectLatest {
                populateList(it)
            }
        }

        viewModel.fetchCustomers()
    }

    private fun onNetworkError(error: CustomersNetworkState.ErrorLoading) {
        list.hideLoading()
        showToast(getString(R.string.generic_error_message, error.exception.message))
    }

    private fun onNetworkRequestFinished() {
        list.hideLoading()
        adapter.refresh()
    }

    private fun initList() {
        adapter = CustomerListAdapter()
        val recyclerView = list.getList()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(ListDivider(requireContext()))
        recyclerView.adapter = adapter

        list.onRefreshRequested { viewModel.fetchCustomers() }
    }

    private fun populateList(customers: PagingData<Customer>) {
        list.showList()
        viewLifecycleOwner.lifecycleScope.launch(MyDispatchers.Main) {
            adapter.submitData(customers)
        }
    }

    private fun initViews(view: View) {
        list = view.findViewById(R.id.customer_list)
    }
}