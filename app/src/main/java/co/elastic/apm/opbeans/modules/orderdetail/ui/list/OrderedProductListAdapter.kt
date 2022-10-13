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
package co.elastic.apm.opbeans.modules.orderdetail.ui.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import co.elastic.apm.opbeans.modules.orderdetail.data.OrderedProductSateItem

class OrderedProductListAdapter :
    ListAdapter<OrderedProductSateItem, OrderedProductViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderedProductViewHolder {
        return OrderedProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: OrderedProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.setData(product)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<OrderedProductSateItem>() {

            override fun areItemsTheSame(
                oldItem: OrderedProductSateItem,
                newItem: OrderedProductSateItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: OrderedProductSateItem,
                newItem: OrderedProductSateItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}