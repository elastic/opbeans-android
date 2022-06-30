package co.elastic.apm.opbeans.modules.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.modules.home.ui.HomeState
import co.elastic.apm.opbeans.modules.home.ui.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var errorContainer: View
    private lateinit var loadingContainer: View
    private lateinit var productList: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    is HomeState.Loading -> showLoading()
                    else -> showError()
                }
            }
        }

        viewModel.fetchProducts()
    }

    private fun initViews(view: View) {
        errorContainer = view.findViewById(R.id.error_container)
        loadingContainer = view.findViewById(R.id.loading_container)
        productList = view.findViewById(R.id.product_list)
    }

    private fun showProductList() {
        productList.visibility = View.VISIBLE
        errorContainer.visibility = View.INVISIBLE
        loadingContainer.visibility = View.INVISIBLE
    }

    private fun showLoading() {
        loadingContainer.visibility = View.VISIBLE
        errorContainer.visibility = View.INVISIBLE
        productList.visibility = View.INVISIBLE
    }

    private fun showError() {
        errorContainer.visibility = View.VISIBLE
        loadingContainer.visibility = View.INVISIBLE
        productList.visibility = View.INVISIBLE
    }
}