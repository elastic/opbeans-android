package co.elastic.apm.opbeans.modules.customers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.customers.ui.CustomerState
import co.elastic.apm.opbeans.modules.customers.ui.CustomersViewModel
import co.elastic.apm.opbeans.modules.customers.ui.list.CustomerListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CustomersFragment : Fragment(R.layout.fragment_customers) {

    private val viewModel: CustomersViewModel by viewModels()
    private lateinit var list: LoadableList
    private lateinit var adapter: CustomerListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListAdapter()

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    is CustomerState.Loading -> list.showLoading()
                    is CustomerState.ErrorLoading -> list.showError(it.exception)
                    is CustomerState.FinishedLoading -> populateList(it.customers)
                }
            }
        }

        viewModel.fetchCustomers()
    }

    private fun initListAdapter() {
        adapter = CustomerListAdapter()
        val list = list.getList()
        list.layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.list_item_divider, null))
        list.addItemDecoration(dividerItemDecoration)
        list.adapter = adapter
    }

    private fun populateList(customers: List<Customer>) {
        list.showList()
        adapter.submitList(customers)
    }

    private fun initViews(view: View) {
        list = view.findViewById(R.id.customer_list)
    }
}