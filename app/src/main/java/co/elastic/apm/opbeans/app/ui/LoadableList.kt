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
package co.elastic.apm.opbeans.app.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import co.elastic.apm.opbeans.R

class LoadableList @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val swipeToRefresh: SwipeRefreshLayout
    private val list: RecyclerView
    private val emptyListContainer: View
    private val emptyListMessage: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.loadable_list, this, true)
        list = findViewById(R.id.list)
        swipeToRefresh = findViewById(R.id.swipe_to_refresh)
        emptyListContainer = findViewById(R.id.empty_list_container)
        emptyListMessage = findViewById(R.id.empty_list_message)
    }

    fun getList(): RecyclerView = list

    fun showList() {
        list.visibility = View.VISIBLE
        emptyListContainer.visibility = View.INVISIBLE
        hideLoading()
    }

    fun showError(e: Throwable) {
        showEmptyMessage(resources.getString(R.string.generic_error_message, e.message))
    }

    fun onRefreshRequested(action: () -> Unit) {
        swipeToRefresh.setOnRefreshListener {
            action.invoke()
        }
    }

    fun showEmptyMessage(message: String) {
        emptyListMessage.text = message
        emptyListContainer.visibility = View.VISIBLE
        list.visibility = View.INVISIBLE
        hideLoading()
    }

    fun showLoading() {
        swipeToRefresh.isRefreshing = true
    }

    fun hideLoading() {
        swipeToRefresh.isRefreshing = false
    }
}