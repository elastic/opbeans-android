package co.elastic.apm.opbeans.app.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.elastic.apm.opbeans.R

class LoadableList @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val list: RecyclerView
    private val errorContainer: View
    private val loadingContainer: View
    private val errorDescription: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.loadable_list, this, true)
        list = findViewById(R.id.list)
        errorContainer = findViewById(R.id.error_container)
        errorDescription = findViewById(R.id.error_description)
        loadingContainer = findViewById(R.id.loading_container)
    }

    fun getList(): RecyclerView = list

    fun showList() {
        list.visibility = View.VISIBLE
        errorContainer.visibility = View.INVISIBLE
        loadingContainer.visibility = View.INVISIBLE
    }

    fun showLoading() {
        loadingContainer.visibility = View.VISIBLE
        errorContainer.visibility = View.INVISIBLE
        list.visibility = View.INVISIBLE
    }

    fun showError(e: Exception) {
        errorDescription.text = e.message ?: ""
        errorContainer.visibility = View.VISIBLE
        loadingContainer.visibility = View.INVISIBLE
        list.visibility = View.INVISIBLE
    }
}