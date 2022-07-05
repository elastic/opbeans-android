package co.elastic.apm.opbeans.modules.cart

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.CartItem
import co.elastic.apm.opbeans.modules.cart.ui.CartViewModel
import co.elastic.apm.opbeans.modules.cart.ui.CartViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {

    private val viewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initViews()

        lifecycleScope.launch {
            viewModel.cartState.collectLatest {
                when (it) {
                    is CartViewState.Loading -> showLoading()
                    is CartViewState.ErrorLoading -> showErrorMessage(it.e)
                    is CartViewState.FinishedLoading -> showCartItems(it.items)
                }
            }
        }
    }

    private fun initViews() {

    }

    private fun showCartItems(items: List<CartItem>) {
        TODO("Not yet implemented")
    }

    private fun showErrorMessage(e: Throwable) {

    }

    private fun showLoading() {
        TODO("Not yet implemented")
    }
}