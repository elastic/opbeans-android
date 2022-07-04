package co.elastic.apm.opbeans.modules.products

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.Product
import co.elastic.apm.opbeans.app.ui.LoadableList
import co.elastic.apm.opbeans.modules.products.ui.ProductsState
import co.elastic.apm.opbeans.modules.products.ui.ProductsViewModel
import co.elastic.apm.opbeans.modules.products.ui.products.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_products), MenuProvider {

    private val viewModel: ProductsViewModel by viewModels()
    private val menuHost: MenuHost by lazy { requireActivity() }
    private lateinit var productList: LoadableList
    private lateinit var productListAdapter: ProductListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListAdapter()
        initMenu()

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    is ProductsState.ProductsLoaded -> populateProductList(it.products)
                    is ProductsState.Loading -> productList.showLoading()
                    is ProductsState.Error -> productList.showError(it.e)
                }
            }
        }

        viewModel.fetchProducts()
    }

    private fun initMenu() {
        menuHost.addMenuProvider(this, viewLifecycleOwner)
    }

    private fun initListAdapter() {
        productListAdapter = ProductListAdapter()
        productList.getList().layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.list_item_divider, null))
        productList.getList().addItemDecoration(dividerItemDecoration)
        productList.getList().adapter = productListAdapter
    }

    private fun initViews(view: View) {
        productList = view.findViewById(R.id.product_list)
    }

    private fun populateProductList(products: List<Product>) {
        productList.showList()
        productListAdapter.submitList(products)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.home_options_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.shopping_cart -> Toast.makeText(requireContext(), "Yay!", Toast.LENGTH_SHORT)
                .show()
        }

        return true
    }
}