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