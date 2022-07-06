package co.elastic.apm.opbeans.modules.productdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.ProductDetail
import co.elastic.apm.opbeans.modules.productdetail.ui.ProductDetailState
import co.elastic.apm.opbeans.modules.productdetail.ui.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailActivity : AppCompatActivity() {

    private val viewModel: ProductDetailViewModel by viewModels()

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

    private fun showProductDetail(product: ProductDetail) {
        TODO("Not yet implemented")
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