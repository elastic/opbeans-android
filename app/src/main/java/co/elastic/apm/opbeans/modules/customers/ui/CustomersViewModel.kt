package co.elastic.apm.opbeans.modules.customers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.elastic.apm.opbeans.app.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomersViewModel @Inject constructor(private val customerRepository: CustomerRepository) :
    ViewModel() {

    private val internalState: MutableStateFlow<CustomerState> =
        MutableStateFlow(CustomerState.Loading)
    val state = internalState.asStateFlow()

    fun fetchCustomers() {
        viewModelScope.launch {
            try {
                internalState.update { CustomerState.Loading }
                val customers = customerRepository.getCustomers()
                internalState.update { CustomerState.FinishedLoading(customers) }
            } catch (e: Exception) {
                internalState.update { CustomerState.ErrorLoading(e) }
            }
        }
    }
}