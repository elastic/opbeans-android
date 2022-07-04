package co.elastic.apm.opbeans.modules.orders.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem

class OrderViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val customerName: TextView = itemView.findViewById(R.id.order_item_customer_name)
    private val orderId: TextView = itemView.findViewById(R.id.order_item_id)
    private val orderDate: TextView = itemView.findViewById(R.id.order_item_date)

    companion object {
        fun create(parent: ViewGroup): OrderViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.order_item, parent, false)
            return OrderViewHolder(view)
        }
    }

    fun setData(order: OrderStateItem) {
        orderId.text = order.id
        customerName.text = order.customerName
        orderDate.text = order.date
    }
}