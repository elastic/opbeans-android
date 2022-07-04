package co.elastic.apm.opbeans.modules.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem
import co.elastic.apm.opbeans.modules.orders.ui.OrdersState
import co.elastic.apm.opbeans.modules.orders.ui.OrdersViewModel
import co.elastic.apm.opbeans.modules.orders.ui.list.OrderListAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var list: LoadableList
    private lateinit var adapter: OrderListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListAdapter()

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    is OrdersState.Loading -> list.showLoading()
                    is OrdersState.ErrorLoading -> list.showError(it.exception)
                    is OrdersState.FinishedLoading -> populateList(it.orders)
                }
            }
        }

        viewModel.fetchOrders()
    }

    private fun populateList(orders: List<OrderStateItem>) {
        list.showList()
        adapter.submitList(orders)
    }

    private fun initListAdapter() {
        adapter = OrderListAdapter()
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

    private fun initViews(view: View) {
        list = view.findViewById(R.id.order_list)
    }
}