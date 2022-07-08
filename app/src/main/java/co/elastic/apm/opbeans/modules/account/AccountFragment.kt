package co.elastic.apm.opbeans.modules.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import co.elastic.apm.opbeans.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment(R.layout.fragment_account) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}