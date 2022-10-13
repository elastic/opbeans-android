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