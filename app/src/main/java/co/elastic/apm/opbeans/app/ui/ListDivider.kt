package co.elastic.apm.opbeans.app.ui

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.elastic.apm.opbeans.R

class ListDivider(context: Context) :
    DividerItemDecoration(context, LinearLayoutManager.VERTICAL) {

    init {
        setDrawable(ContextCompat.getDrawable(context, R.drawable.list_item_divider)!!)
    }
}