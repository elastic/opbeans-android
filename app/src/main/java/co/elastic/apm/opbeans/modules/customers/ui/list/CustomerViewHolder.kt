package co.elastic.apm.opbeans.modules.customers.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.Customer

class CustomerViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val customerName = itemView.findViewById<TextView>(R.id.customer_name)
    private val companyName = itemView.findViewById<TextView>(R.id.company_name)
    private val customerEmail = itemView.findViewById<TextView>(R.id.customer_email)
    private val customerLocation = itemView.findViewById<TextView>(R.id.customer_location)

    companion object {
        fun create(parent: ViewGroup): CustomerViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.customer_item, parent, false)
            return CustomerViewHolder(view)
        }
    }

    fun setData(customer: Customer) {
        customerName.text = customer.fullName
        companyName.text = customer.companyName
        customerEmail.text = customer.email
        customerLocation.text = customer.location
    }
}