/* 
Licensed to Elasticsearch B.V. under one or more contributor
license agreements. See the NOTICE file distributed with
this work for additional information regarding copyright
ownership. Elasticsearch B.V. licenses this file to you under
the Apache License, Version 2.0 (the "License"); you may
not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License. 
*/
package co.elastic.apm.opbeans

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import co.elastic.apm.opbeans.modules.account.AccountFragment
import co.elastic.apm.opbeans.modules.cart.CartActivity
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
        private const val ACCOUNT_TAG = "account_fragment_tag"
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
                R.id.account_item -> showMyAccount()
                else -> false
            }
        }
    }

    private fun showMyAccount(): Boolean {
        showFragment(ACCOUNT_TAG) {
            AccountFragment()
        }

        return true
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
            R.id.shopping_cart -> CartActivity.launch(this)
        }

        return true
    }
}