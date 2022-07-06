package co.elastic.apm.opbeans.modules.productdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
class ProductDetailActivity : AppCompatActivity() {

    private val viewModel: ProductDetailViewModel by viewModels()
    private lateinit var title: TextView
    private lateinit var type: TextView
    private lateinit var description: TextView
    private lateinit var image: ImageView

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"

        fun launch(context: Context, productId: Int) {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(PARAM_PRODUCT_ID, productId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        initViews()

        val productId = getProductId()

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                when (it) {
                    is ProductDetailState.Loading -> showLoading()
                    is ProductDetailState.ErrorLoading -> showErrorLoading(it.e)
                    is ProductDetailState.FinishedLoading -> showProductDetail(it.product)
                }
            }
        }

        viewModel.fetchProduct(productId)
    }

    private fun initViews() {
        title = findViewById(R.id.product_detail_title)
        type = findViewById(R.id.product_detail_type)
        description = findViewById(R.id.product_detail_description)
        image = findViewById(R.id.product_detail_image)
    }

    private fun showProductDetail(product: ProductDetail) {
        title.text = product.name
        type.text = product.type
        description.text = product.description
        Glide.with(this).load(product.imageUrl).into(image)
    }

    private fun showErrorLoading(e: Throwable) {
        TODO("Not yet implemented")
    }

    private fun showLoading() {
        TODO("Not yet implemented")
    }

    private fun getProductId(): Int {
        val param = intent.getIntExtra(PARAM_PRODUCT_ID, -1)
        if (param == -1) {
            throw IllegalArgumentException("No product id available")
        }
        return param
    }
}