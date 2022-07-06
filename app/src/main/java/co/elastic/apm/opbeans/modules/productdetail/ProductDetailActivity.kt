package co.elastic.apm.opbeans.modules.productdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.elastic.apm.opbeans.R

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
    }
}