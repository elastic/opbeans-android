package co.elastic.apm.opbeans.modules.account

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.ui.LoadableList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var loadingContainer: View
    private lateinit var errorContainer: View
    private lateinit var contentContainer: View
    private lateinit var userName: TextView
    private lateinit var companyAndLocation: TextView
    private lateinit var userEmail: TextView
    private lateinit var list: LoadableList
    private lateinit var containers: List<View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        loadingContainer = view.findViewById(R.id.account_loading_container)
        errorContainer = view.findViewById(R.id.account_error_container)
        contentContainer = view.findViewById(R.id.account_content_container)
        userName = view.findViewById(R.id.account_user_name)
        companyAndLocation = view.findViewById(R.id.account_user_company_and_location)
        userEmail = view.findViewById(R.id.account_user_email)
        list = view.findViewById(R.id.account_orders_list)
        containers = listOf(loadingContainer, errorContainer, contentContainer)
    }
}