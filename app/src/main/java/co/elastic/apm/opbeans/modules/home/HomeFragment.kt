package co.elastic.apm.opbeans.modules.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.modules.home.ui.HomeState
import co.elastic.apm.opbeans.modules.home.ui.HomeViewModel
import co.elastic.apm.opbeans.modules.home.ui.products.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var errorContainer: View
    private lateinit var loadingContainer: View
    private lateinit var productList: RecyclerView
    private lateinit var productListAdapter: ProductListAdapter
    private lateinit var errorDescription: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListAdapter()

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    is HomeState.ProductsLoaded -> populateProductList(it.products)
                    is HomeState.Loading -> showLoading()
                    is HomeState.Error -> showError(it.e)
                }
            }
        }

        viewModel.fetchProducts()
    }

    private fun initListAdapter() {
        productListAdapter = ProductListAdapter()
        productList.adapter = productListAdapter
    }

    private fun initViews(view: View) {
        errorContainer = view.findViewById(R.id.error_container)
        loadingContainer = view.findViewById(R.id.loading_container)
        productList = view.findViewById(R.id.product_list)
        errorDescription = view.findViewById(R.id.error_description)
    }

    private fun populateProductList(products: List<Product>) {
        showProductList()
        productListAdapter.submitList(products)
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

    private fun showError(e: Exception) {
        errorDescription.text = e.message ?: ""
        errorContainer.visibility = View.VISIBLE
        loadingContainer.visibility = View.INVISIBLE
        productList.visibility = View.INVISIBLE
    }
}