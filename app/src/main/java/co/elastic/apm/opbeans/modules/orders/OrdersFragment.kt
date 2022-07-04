package co.elastic.apm.opbeans.modules.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.ui.LoadableList

class OrdersFragment : Fragment(R.layout.fragment_orders) {

    private lateinit var list: LoadableList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListAdapter()
    }

    private fun initListAdapter() {
        TODO("Not yet implemented")
    }

    private fun initViews(view: View) {
        list = view.findViewById(R.id.order_list)
    }
}