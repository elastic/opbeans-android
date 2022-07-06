package co.elastic.apm.opbeans.modules.productdetail.ui

import androidx.lifecycle.ViewModel
import co.elastic.apm.opbeans.app.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {
}