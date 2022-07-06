package co.elastic.apm.opbeans.modules.cart

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.cart.ui.CartViewModel
import co.elastic.apm.opbeans.modules.cart.ui.CartViewState
import co.elastic.apm.opbeans.modules.cart.ui.list.CartListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {

    private val viewModel: CartViewModel by viewModels()
    private lateinit var list: LoadableList
    private lateinit var adapter: CartListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initViews()
        initList()

        lifecycleScope.launch {
            viewModel.cartState.collectLatest {
                when (it) {
                    is CartViewState.Loading -> list.showLoading()
                    is CartViewState.ErrorLoading -> list.showError(it.e)
                    is CartViewState.FinishedLoading -> showCartItems(it.items)
                }
            }
        }
    }

    private fun initViews() {
        list = findViewById(R.id.cart_items_list)
    }

    private fun initList() {
        adapter = CartListAdapter()
        list.getList().layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            this,
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.list_item_divider, null))
        list.getList().addItemDecoration(dividerItemDecoration)
        list.getList().adapter = adapter
    }

    private fun showCartItems(items: List<CartItem>) {
        list.showList()
        adapter.submitList(items)
    }
}