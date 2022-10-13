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

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem

class OrderListAdapter(private val onItemClick: (Int) -> Unit) :
    PagingDataAdapter<OrderStateItem, OrderViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)!!
        holder.setData(order)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(order.id)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<OrderStateItem>() {

            override fun areItemsTheSame(
                oldItem: OrderStateItem,
                newItem: OrderStateItem
            ): Boolean {
                return oldItem.displayId == newItem.displayId
            }

            override fun areContentsTheSame(
                oldItem: OrderStateItem,
                newItem: OrderStateItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}