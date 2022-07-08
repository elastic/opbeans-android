package co.elastic.apm.opbeans.modules.orderdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.elastic.apm.opbeans.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
    }
}