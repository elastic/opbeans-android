package co.elastic.apm.opbeans.modules.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.tools.showToast
import co.elastic.apm.opbeans.app.ui.ListDivider
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.orderdetail.OrderDetailActivity
import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem
import co.elastic.apm.opbeans.modules.orders.ui.OrdersNetworkState
import co.elastic.apm.opbeans.modules.orders.ui.OrdersViewModel
import co.elastic.apm.opbeans.modules.orders.ui.list.OrderListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var list: LoadableList
    private lateinit var adapter: OrderListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initList()

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    is OrdersNetworkState.Loading -> list.showLoading()
                    is OrdersNetworkState.ErrorLoading -> onNetworkError(it)
                    is OrdersNetworkState.FinishedLoading -> onNetworkRequestFinished()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.orders.collectLatest {
                populateList(it)
            }
        }

        viewModel.fetchOrders()
    }

    private fun onNetworkError(error: OrdersNetworkState.ErrorLoading) {
        list.hideLoading()
        showToast(getString(R.string.generic_error_message, error.exception.message))
    }

    private fun onNetworkRequestFinished() {
        list.hideLoading()
        adapter.refresh()
    }

    private fun populateList(orders: PagingData<OrderStateItem>) {
        list.showList()
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.submitData(orders)
        }
    }

    private fun initList() {
        adapter = OrderListAdapter(::onOrderItemClicked)
        val recyclerView = list.getList()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(ListDivider(requireContext()))
        recyclerView.adapter = adapter

        list.onRefreshRequested { viewModel.fetchOrders() }
    }

    private fun onOrderItemClicked(orderId: Int) {
        OrderDetailActivity.launch(requireContext(), orderId)
    }

    private fun initViews(view: View) {
        list = view.findViewById(R.id.order_list)
    }
}