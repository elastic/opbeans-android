package co.elastic.apm.opbeans.modules.orderdetail.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.elastic.apm.opbeans.R
import co.elastic.apm.opbeans.modules.orderdetail.data.OrderedProductSateItem
import com.bumptech.glide.Glide

class OrderedProductViewHolder private constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val title = itemView.findViewById<TextView>(R.id.product_title)
    private val amount = itemView.findViewById<TextView>(R.id.product_amount)
    private val price = itemView.findViewById<TextView>(R.id.product_price)
    private val image = itemView.findViewById<ImageView>(R.id.product_image)

    companion object {
        fun create(container: ViewGroup): OrderedProductViewHolder {
            val view = LayoutInflater.from(container.context)
                .inflate(R.layout.ordered_product_item, container, false)

            return OrderedProductViewHolder(view)
        }
    }

    fun setData(product: OrderedProductSateItem) {
        title.text = product.name
        amount.text = product.amount
        price.text = product.price
        Glide.with(image).load(product.imageUrl).into(image)
    }
}