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