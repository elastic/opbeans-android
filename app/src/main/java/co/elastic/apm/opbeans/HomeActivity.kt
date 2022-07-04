package co.elastic.apm.opbeans

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import co.elastic.apm.opbeans.modules.customers.CustomersFragment
import co.elastic.apm.opbeans.modules.products.ProductsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    companion object {
        private const val PRODUCTS_TAG = "products_fragment_tag"
        private const val CUSTOMERS_TAG = "customers_fragment_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()
        setUpBottomNavigation()
        showProducts()
    }

    private fun setUpBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.products_item -> showProducts()
                R.id.customers_item -> showCustomers()
                else -> false
            }
        }
    }

    private fun showProducts(): Boolean {
        showFragment(PRODUCTS_TAG) {
            ProductsFragment()
        }

        return true
    }

    private fun showCustomers(): Boolean {
        showFragment(CUSTOMERS_TAG) {
            CustomersFragment()
        }

        return true
    }

    private fun initViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
    }

    private fun showFragment(tag: String, fragmentCreation: (() -> Fragment)) {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = findOrCreateFragment(tag, fragmentCreation, transaction)
        showOnly(fragment, transaction)

        transaction.commit()
    }

    private fun showOnly(fragment: Fragment, transaction: FragmentTransaction) {
        supportFragmentManager.fragments.forEach {
            if (it == fragment) {
                transaction.show(it)
            } else {
                transaction.hide(it)
            }
        }
    }

    private fun findOrCreateFragment(
        tag: String,
        fragmentCreation: () -> Fragment,
        transaction: FragmentTransaction
    ): Fragment {
        val existingFragment = supportFragmentManager.findFragmentByTag(tag)
        return if (existingFragment != null) {
            existingFragment
        } else {
            val fragment = fragmentCreation.invoke()
            transaction.add(R.id.fragment_container, fragment, tag)
            fragment
        }
    }
}