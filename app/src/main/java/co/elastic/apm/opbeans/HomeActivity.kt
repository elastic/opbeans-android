package co.elastic.apm.opbeans

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import co.elastic.apm.opbeans.modules.customers.CustomersFragment
import co.elastic.apm.opbeans.modules.orders.OrdersFragment
import co.elastic.apm.opbeans.modules.products.ProductsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), MenuProvider {

    private lateinit var bottomNavigation: BottomNavigationView

    companion object {
        private const val PRODUCTS_TAG = "products_fragment_tag"
        private const val CUSTOMERS_TAG = "customers_fragment_tag"
        private const val ORDERS_TAG = "orders_fragment_tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViews()
        initMenu()
        setUpBottomNavigation()
        showProducts()
    }

    private fun initMenu() {
        addMenuProvider(this, this)
    }

    private fun setUpBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.products_item -> showProducts()
                R.id.customers_item -> showCustomers()
                R.id.orders_item -> showOrders()
                else -> false
            }
        }
    }

    private fun showOrders(): Boolean {
        showFragment(ORDERS_TAG) {
            OrdersFragment()
        }

        return true
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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.home_options_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.shopping_cart -> Toast.makeText(this, "Yay!", Toast.LENGTH_SHORT)
                .show()
        }

        return true
    }
}