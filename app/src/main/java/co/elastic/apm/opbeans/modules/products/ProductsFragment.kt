package co.elastic.apm.opbeans.modules.products

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.productdetail.ProductDetailActivity
import co.elastic.apm.opbeans.modules.products.ui.ProductsViewModel
import co.elastic.apm.opbeans.modules.products.ui.products.ProductListAdapter
import co.elastic.apm.opbeans.modules.products.ui.state.NetworkRequestState
import co.elastic.apm.opbeans.modules.products.ui.state.ProductsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products) {

    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var productList: LoadableList
    private lateinit var productListAdapter: ProductListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListAdapter()

        lifecycleScope.launch {
            viewModel.products.collectLatest {
                when (it) {
                    is ProductsState.ProductsLoaded -> populateProductList(it.products)
                    is ProductsState.Loading -> productList.showLoading()
                    is ProductsState.Error -> productList.showError(it.e)
                }
            }
            viewModel.networkRequestState.collectLatest {
                if (it is NetworkRequestState.Failed) {
                    showNetworkErrorMessage(it.e)
                }
            }
        }
    }

    private fun showNetworkErrorMessage(e: Throwable) {
        val message = getString(R.string.error_message_products_request, e.message)
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun initListAdapter() {
        productListAdapter = ProductListAdapter(::onItemClicked)
        productList.getList().layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.list_item_divider, null))
        productList.getList().addItemDecoration(dividerItemDecoration)
        productList.getList().adapter = productListAdapter
    }

    private fun onItemClicked(productId: Int, productName: String) {
        ProductDetailActivity.launch(requireContext(), productId, productName)
    }

    private fun initViews(view: View) {
        productList = view.findViewById(R.id.product_list)
    }

    private fun populateProductList(products: List<Product>) {
        productList.showList()
        productListAdapter.submitList(products)
    }
}