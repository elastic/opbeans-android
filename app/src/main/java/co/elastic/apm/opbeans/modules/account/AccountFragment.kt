package co.elastic.apm.opbeans.modules.account

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.ui.ListDivider
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.account.data.AccountStateScreenItem
import co.elastic.apm.opbeans.modules.account.state.AccountState
import co.elastic.apm.opbeans.modules.account.ui.AccountViewModel
import co.elastic.apm.opbeans.modules.orderdetail.OrderDetailActivity
import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem
import co.elastic.apm.opbeans.modules.orders.ui.list.OrderListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private val viewModel: AccountViewModel by viewModels()
    private lateinit var loadingContainer: View
    private lateinit var errorContainer: View
    private lateinit var contentContainer: View
    private lateinit var errorMessage: TextView
    private lateinit var userName: TextView
    private lateinit var companyAndLocation: TextView
    private lateinit var userEmail: TextView
    private lateinit var list: LoadableList
    private lateinit var adapter: OrderListAdapter
    private lateinit var containers: List<View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initList()

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    is AccountState.LoadingScreen -> showOnly(loadingContainer)
                    is AccountState.ErrorLoadingScreen -> showScreenLoadError(it.e)
                    is AccountState.FinishedLoadingScreen -> onScreenLoadedSuccessfully(it.data)
                }
            }
        }

        viewModel.fetchScreen()
    }

    private fun initViews(view: View) {
        loadingContainer = view.findViewById(R.id.account_loading_container)
        errorContainer = view.findViewById(R.id.account_error_container)
        contentContainer = view.findViewById(R.id.account_content_container)
        userName = view.findViewById(R.id.account_user_name)
        companyAndLocation = view.findViewById(R.id.account_user_company_and_location)
        userEmail = view.findViewById(R.id.account_user_email)
        errorMessage = view.findViewById(R.id.account_error_message)
        list = view.findViewById(R.id.account_orders_list)
        containers = listOf(loadingContainer, errorContainer, contentContainer)
    }

    private fun initList() {
        adapter = OrderListAdapter(::onOrderItemClicked)
        val recyclerView = list.getList()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(ListDivider(requireContext()))
        recyclerView.adapter = adapter

        list.onRefreshRequested {
            adapter.refresh()
        }
        adapter.addLoadStateListener {
            if (isEmpty(it)) {
                list.showEmptyMessage(getString(R.string.account_no_orders_available))
            }
        }
    }

    private fun isEmpty(loadStates: CombinedLoadStates): Boolean {
        return loadStates.append is LoadState.NotLoading && loadStates.append.endOfPaginationReached
    }

    private fun onOrderItemClicked(orderId: Int) {
        OrderDetailActivity.launch(requireContext(), orderId)
    }

    private fun onScreenLoadedSuccessfully(screenItem: AccountStateScreenItem) {
        showOnly(contentContainer)
        val customer = screenItem.customer
        userName.text = customer.fullName
        userEmail.text = customer.email
        companyAndLocation.text = "${customer.companyName} / ${customer.location}"

        list.showList()
        attachOrdersToList()
    }

    private fun attachOrdersToList() {
        lifecycleScope.launch {
            viewModel.orders.collectLatest {
                submitOrderListData(it)
            }
        }
    }

    private suspend fun submitOrderListData(it: PagingData<OrderStateItem>) {
        list.hideLoading()
        adapter.submitData(it)
    }

    private fun showScreenLoadError(e: Throwable) {
        showOnly(errorContainer)
        errorMessage.text = getString(R.string.generic_error_message, e.message)
    }

    private fun showOnly(container: View) {
        containers.forEach {
            if (it == container) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.INVISIBLE
            }
        }
    }
}