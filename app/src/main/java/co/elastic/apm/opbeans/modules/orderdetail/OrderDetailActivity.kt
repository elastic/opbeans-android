package co.elastic.apm.opbeans.modules.orderdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.tools.MyDispatchers
import co.elastic.apm.opbeans.app.ui.ListDivider
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.orderdetail.data.OrderDetailStateItem
import co.elastic.apm.opbeans.modules.orderdetail.ui.OrderDetailState
import co.elastic.apm.opbeans.modules.orderdetail.ui.OrderDetailViewModel
import co.elastic.apm.opbeans.modules.orderdetail.ui.list.OrderedProductListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderDetailActivity : AppCompatActivity(), MenuProvider {

    private val viewModel: OrderDetailViewModel by viewModels()
    private lateinit var list: LoadableList
    private lateinit var total: TextView
    private lateinit var adapter: OrderedProductListAdapter
    private var orderId: Int = -1

    companion object {
        private const val PARAM_ORDER_ID = "order_id"

        fun launch(context: Context, orderId: Int) {
            val intent = Intent(context, OrderDetailActivity::class.java)
            intent.putExtra(PARAM_ORDER_ID, orderId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        initViews()
        initList()
        setOrderId()
        setUpToolbar()

        lifecycleScope.launch(MyDispatchers.Main) {
            viewModel.state.collectLatest {
                when (it) {
                    is OrderDetailState.Loading -> list.showLoading()
                    is OrderDetailState.ErrorLoading -> list.showError(it.e)
                    is OrderDetailState.FinishedLoading -> populateData(it.value)
                }
            }
        }

        viewModel.fetchOrderDetail(orderId)
    }

    private fun setUpToolbar() {
        supportActionBar?.title = getString(R.string.order_detail_title, orderId)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addMenuProvider(this, this)
    }

    private fun setOrderId() {
        orderId = intent.getIntExtra(PARAM_ORDER_ID, -1)
        if (orderId == -1) {
            throw IllegalArgumentException("No order id found")
        }
    }

    private fun initViews() {
        list = findViewById(R.id.orders_list)
        total = findViewById(R.id.order_detail_total)
    }

    private fun initList() {
        adapter = OrderedProductListAdapter()
        val recyclerView = list.getList()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(ListDivider(this))
        recyclerView.adapter = adapter

        list.onRefreshRequested { viewModel.fetchOrderDetail(orderId) }
    }

    private fun populateData(value: OrderDetailStateItem) {
        total.text = value.totalPrice
        list.showList()
        adapter.submitList(value.products)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return false
    }
}