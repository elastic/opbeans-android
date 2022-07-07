package co.elastic.apm.opbeans.modules.productdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.ProductDetail
import co.elastic.apm.opbeans.modules.productdetail.ui.ProductDetailState
import co.elastic.apm.opbeans.modules.productdetail.ui.ProductDetailViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity(), MenuProvider {

    private val viewModel: ProductDetailViewModel by viewModels()
    private var productId: Int = -1
    private lateinit var title: TextView
    private lateinit var type: TextView
    private lateinit var description: TextView
    private lateinit var image: ImageView
    private lateinit var errorContainer: View
    private lateinit var loadingContainer: View
    private lateinit var contentContainer: View
    private lateinit var errorMessage: TextView
    private lateinit var containers: List<View>

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_PRODUCT_NAME = "product_name"

        fun launch(context: Context, productId: Int, productName: String) {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(PARAM_PRODUCT_ID, productId)
            intent.putExtra(PARAM_PRODUCT_NAME, productName)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        initViews()
        initOptionsMenu()

        productId = getProductId()
        setUpToolbar()


        lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    is ProductDetailState.Loading -> showLoading()
                    is ProductDetailState.ErrorLoading -> showErrorLoading(it.e)
                    is ProductDetailState.FinishedLoading -> showProductDetail(it.product)
                    is ProductDetailState.AddedToCart -> showAddToCartSuccessMessage()
                    is ProductDetailState.AlreadyInCart -> showProductAlreadyInCartMessage()
                }
            }
        }

        viewModel.fetchProduct(productId)
    }

    private fun setUpToolbar() {
        getProductNameOrNull()?.let { name ->
            supportActionBar?.title = name
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getProductNameOrNull(): String? {
        return intent.getStringExtra(PARAM_PRODUCT_NAME)
    }

    private fun initOptionsMenu() {
        addMenuProvider(this, this)
    }

    private fun initViews() {
        title = findViewById(R.id.product_detail_title)
        type = findViewById(R.id.product_detail_type)
        description = findViewById(R.id.product_detail_description)
        image = findViewById(R.id.product_detail_image)
        contentContainer = findViewById(R.id.product_detail_content)
        errorContainer = findViewById(R.id.product_detail_error_container)
        loadingContainer = findViewById(R.id.product_detail_loading_container)
        errorMessage = findViewById(R.id.product_detail_error_message)
        containers = listOf(errorContainer, loadingContainer, contentContainer)
    }

    private fun showProductDetail(product: ProductDetail) {
        showContent()
        title.text = product.name
        type.text = product.type
        description.text = product.description
        Glide.with(this).load(product.imageUrl).into(image)
    }

    private fun showContent() {
        showOnly(contentContainer)
    }

    private fun showErrorLoading(e: Throwable) {
        showOnly(errorContainer)
        errorMessage.text = e.message
    }

    private fun showLoading() {
        showOnly(loadingContainer)
    }

    private fun showOnly(container: View) {
        containers.forEach {
            if (it == container) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.INVISIBLE
            }
        }
    }

    private fun getProductId(): Int {
        val param = intent.getIntExtra(PARAM_PRODUCT_ID, -1)
        if (param == -1) {
            throw IllegalArgumentException("No product id available")
        }
        return param
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.product_detail_options_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.add_to_cart -> addItemToCart()
            android.R.id.home -> onBackPressed()
        }

        return true
    }

    private fun addItemToCart() {
        viewModel.addProductToCart(productId)
    }

    private fun showProductAlreadyInCartMessage() {
        showToastMessage(R.string.product_detail_already_added_to_cart)
    }

    private fun showAddToCartSuccessMessage() {
        showToastMessage(R.string.product_detail_added_to_cart)
    }

    private fun showToastMessage(@StringRes stringId: Int) {
        Toast.makeText(this, getString(stringId), Toast.LENGTH_LONG)
            .show()
    }
}