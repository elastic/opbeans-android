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
        swipeToRefresh.isRefreshing = false
        emptyListContainer.visibility = View.INVISIBLE
    }

    fun showLoading() {
        swipeToRefresh.isRefreshing = true
        list.visibility = View.INVISIBLE
        emptyListContainer.visibility = View.INVISIBLE
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
        swipeToRefresh.isRefreshing = false
    }
}