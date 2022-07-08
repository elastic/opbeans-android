package co.elastic.apm.opbeans.modules.customers.data.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.elastic.apm.opbeans.app.data.models.Customer
import co.elastic.apm.opbeans.app.data.repository.CustomerRepository
import kotlin.math.max

class CustomerPagingSource(private val customerRepository: CustomerRepository) :
    PagingSource<Int, Customer>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Customer> {
        val offset = params.key ?: 0
        val amount = params.loadSize
        val prevKey = if (offset == 0) null else max(0, offset - amount)

        return try {
            val customers = customerRepository.getSetOfCustomers(offset, amount)
            val nextKey = if (customers.size == amount) offset + amount else null
            LoadResult.Page(customers, prevKey, nextKey)
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Customer>): Int = 0
}