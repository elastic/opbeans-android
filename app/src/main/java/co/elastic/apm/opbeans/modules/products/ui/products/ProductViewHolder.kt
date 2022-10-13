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
package co.elastic.apm.opbeans.modules.products.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.app.data.models.Product
import com.bumptech.glide.Glide

class ProductViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title = itemView.findViewById<TextView>(R.id.product_title)
    private val description = itemView.findViewById<TextView>(R.id.product_description)
    private val image = itemView.findViewById<ImageView>(R.id.product_image)

    companion object {
        fun create(container: ViewGroup): ProductViewHolder {
            val view = LayoutInflater.from(container.context)
                .inflate(R.layout.product_item, container, false)

            return ProductViewHolder(view)
        }
    }

    fun setData(product: Product) {
        title.text = product.name
        description.text = product.type
        Glide.with(image).load(product.imageUrl).into(image)
    }
}