package co.elastic.apm.opbeans.modules.customers.ui.list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import co.elastic.apm.opbeans.app.data.models.Customer

class CustomerListAdapter : PagingDataAdapter<Customer, CustomerViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        return CustomerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.setData(getItem(position)!!)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Customer>() {
            override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
                return oldItem == newItem
            }
        }
    }
}