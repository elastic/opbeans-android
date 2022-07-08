package co.elastic.apm.opbeans.modules.customers.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.elastic.apm.opbeans.app.data.repository.CustomerRepository
import co.elastic.apm.opbeans.modules.customers.data.pager.CustomerPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CustomersViewModel @Inject constructor(private val customerRepository: CustomerRepository) :
    ViewModel() {

    private val internalState: MutableStateFlow<CustomersNetworkState> =
        MutableStateFlow(CustomersNetworkState.Loading)
    val state = internalState.asStateFlow()
    val customers =
        Pager(PagingConfig(pageSize = 10, initialLoadSize = 10, enablePlaceholders = false)) {
            CustomerPagingSource(customerRepository)
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PagingData.empty())

    fun fetchCustomers() {
        viewModelScope.launch {
            try {
                internalState.update { CustomersNetworkState.Loading }
                customerRepository.fetchCustomers()
                internalState.update { CustomersNetworkState.FinishedLoading }
            } catch (e: Exception) {
                internalState.update { CustomersNetworkState.ErrorLoading(e) }
            }
        }
    }
}