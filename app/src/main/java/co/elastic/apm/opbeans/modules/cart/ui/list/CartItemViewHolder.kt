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
package co.elastic.apm.opbeans.modules.cart.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.CartItem
import com.bumptech.glide.Glide

class CartItemViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title = itemView.findViewById<TextView>(R.id.product_title)
    private val description = itemView.findViewById<TextView>(R.id.product_description)
    private val quantity = itemView.findViewById<TextView>(R.id.product_quantity)
    private val image = itemView.findViewById<ImageView>(R.id.product_image)

    companion object {
        fun create(parent: ViewGroup): CartItemViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.cart_product_item, parent, false)
            return CartItemViewHolder(view)
        }
    }

    fun setData(cartItem: CartItem) {
        val product = cartItem.product
        title.text = product.name
        description.text = product.type
        quantity.text = cartItem.amount.toString()
        Glide.with(image).load(product.imageUrl).into(image)
    }
}