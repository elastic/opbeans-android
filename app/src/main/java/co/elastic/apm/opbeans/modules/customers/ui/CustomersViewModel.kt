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

    private val internalState: MutableStateFlow<CustomersState> =
        MutableStateFlow(CustomersState.Loading)
    val state = internalState.asStateFlow()

    fun fetchCustomers() {
        viewModelScope.launch {
            try {
                internalState.update { CustomersState.Loading }
                val customers = customerRepository.getCustomers()
                internalState.update { CustomersState.FinishedLoading(customers) }
            } catch (e: Exception) {
                internalState.update { CustomersState.ErrorLoading(e) }
            }
        }
    }
}