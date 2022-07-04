package co.elastic.apm.opbeans.modules.customers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.customers.ui.CustomerState
import co.elastic.apm.opbeans.modules.customers.ui.CustomersViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CustomersFragment : Fragment(R.layout.fragment_customers) {

    private val viewModel: CustomersViewModel by viewModels()
    private lateinit var list: LoadableList

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
        //todo
    }

    private fun populateList(customers: List<Customer>) {
        //todo
    }

    private fun initViews(view: View) {
        list = view.findViewById(R.id.customer_list)
    }
}