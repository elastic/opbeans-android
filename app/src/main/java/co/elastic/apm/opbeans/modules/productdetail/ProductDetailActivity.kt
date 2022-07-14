package co.elastic.apm.opbeans.modules.productdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.ProductDetail
import co.elastic.apm.opbeans.app.tools.showToast
import co.elastic.apm.opbeans.modules.productdetail.ui.ProductDetailLoadState
import co.elastic.apm.opbeans.modules.productdetail.ui.ProductDetailViewModel
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

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
    private lateinit var addToCartButton: FloatingActionButton
    private lateinit var retry: Button

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
        initClicks()

        productId = getProductId()
        setUpToolbar()
        fetchProduct()
    }

    private fun productLoadCallback(productDetailLoadState: ProductDetailLoadState) {
        when (productDetailLoadState) {
            is ProductDetailLoadState.Loading -> showLoading()
            is ProductDetailLoadState.ErrorLoading -> showErrorLoading(productDetailLoadState.e)
            is ProductDetailLoadState.FinishedLoading -> showProductDetail(productDetailLoadState.product)
        }
    }

    private fun initClicks() {
        addToCartButton.setOnClickListener {
            addItemToCart()
        }
        retry.setOnClickListener {
            fetchProduct()
        }
    }

    private fun fetchProduct() {
        viewModel.fetchProduct(productId, ::productLoadCallback)
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
        addToCartButton = findViewById(R.id.add_to_cart_fab)
        retry = findViewById(R.id.product_retry_button)
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
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }

    private fun addItemToCart() {
        viewModel.addProductToCart(productId) {
            showAddToCartSuccessMessage()
        }
    }

    private fun showAddToCartSuccessMessage() {
        showToast(getString(R.string.product_detail_added_to_cart), Toast.LENGTH_SHORT)
    }
}