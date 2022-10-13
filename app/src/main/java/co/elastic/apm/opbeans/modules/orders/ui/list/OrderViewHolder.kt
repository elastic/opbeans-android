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
        orderId.text = order.displayId
        customerName.text = order.customerName
        orderDate.text = order.date
    }
}