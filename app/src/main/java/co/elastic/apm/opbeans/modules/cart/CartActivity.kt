package co.elastic.apm.opbeans.modules.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.app.ui.ListDivider
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.cart.ui.CartViewModel
import co.elastic.apm.opbeans.modules.cart.ui.list.CartListAdapter
import co.elastic.apm.opbeans.modules.cart.ui.state.CartCheckoutState
import co.elastic.apm.opbeans.modules.cart.ui.state.CartItemsLoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartActivity : AppCompatActivity(), MenuProvider {

    private val viewModel: CartViewModel by viewModels()
    private lateinit var list: LoadableList
    private lateinit var adapter: CartListAdapter
    private lateinit var emptyContainer: View
    private lateinit var checkoutProgressIndicator: View
    private var checkoutOption: MenuItem? = null

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, CartActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initViews()
        initList()
        initOptionsMenu()

        observeCartItems()
        observeCheckout()
    }

    private fun observeCheckout() {
        lifecycleScope.launch {
            viewModel.cartCheckoutState.collectLatest {
                when (it) {
                    is CartCheckoutState.Started -> onCheckoutStarted()
                    is CartCheckoutState.Finished -> onCheckoutFinished()
                    is CartCheckoutState.Error -> onCheckoutFailed(it)
                    is CartCheckoutState.NoItemsToCheckout -> showNoItemsToCheckoutMessage()
                    else -> onCheckoutFinished()
                }
            }
        }
    }

    private fun onCheckoutFailed(it: CartCheckoutState.Error) {
        checkoutOption?.isEnabled = true
        hideCheckoutProgress()
        showCheckoutErrorMessage(it.e)
    }

    private fun onCheckoutStarted() {
        checkoutOption?.isEnabled = false
        showCheckoutProgress()
    }

    private fun showNoItemsToCheckoutMessage() {
        showToastMessage("There are no items to checkout")
    }

    private fun showCheckoutErrorMessage(e: Throwable) {
        showToastMessage("Error while checking out: ${e.message}")
    }

    private fun onCheckoutFinished() {
        checkoutOption?.isEnabled = true
        hideCheckoutProgress()
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showCheckoutProgress() {
        checkoutProgressIndicator.visibility = View.VISIBLE
    }

    private fun hideCheckoutProgress() {
        checkoutProgressIndicator.visibility = View.INVISIBLE
    }

    private fun observeCartItems() {
        lifecycleScope.launch {
            viewModel.cartItemsLoadState.collectLatest {
                when (it) {
                    is CartItemsLoadState.Loading -> list.showLoading()
                    is CartItemsLoadState.ErrorLoading -> list.showError(it.e)
                    is CartItemsLoadState.FinishedLoading -> showCartItems(it.items)
                }
            }
        }
    }

    private fun initViews() {
        list = findViewById(R.id.cart_items_list)
        emptyContainer = findViewById(R.id.cart_empty_container)
        checkoutProgressIndicator = findViewById(R.id.cart_checkout_progress_indicator)
    }

    private fun initList() {
        adapter = CartListAdapter()
        val recyclerView = list.getList()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(ListDivider(this))
        recyclerView.adapter = adapter
    }

    private fun showCartItems(items: List<CartItem>) {
        if (items.isNotEmpty()) {
            showList()
            adapter.submitList(items)
        } else {
            showEmptyCart()
        }
    }

    private fun showEmptyCart() {
        emptyContainer.visibility = View.VISIBLE
        list.visibility = View.INVISIBLE
    }

    private fun showList() {
        list.visibility = View.VISIBLE
        emptyContainer.visibility = View.INVISIBLE
        list.showList()
    }

    private fun initOptionsMenu() {
        addMenuProvider(this, this)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.cart_options_menu, menu)
        checkoutOption = menu.findItem(R.id.cart_checkout_option)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.cart_checkout_option -> viewModel.doCheckout()
        }

        return true
    }
}