package co.elastic.apm.opbeans.modules.orders.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.elastic.apm.opbeans.modules.orders.data.cases.OrderStateItemCase
import co.elastic.apm.opbeans.modules.orders.data.models.OrderStateItem
import kotlin.math.max

class OrdersPagingSource(private val orderStateItemCase: OrderStateItemCase) :
    PagingSource<Int, OrderStateItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderStateItem> {
        val offset = params.key ?: 0
        val amount = params.loadSize
        val prevKey = if (offset == 0) null else max(0, offset - amount)

        return try {
            val orders = orderStateItemCase.getSetOfOrders(offset, amount)
            val nextKey = if (orders.size == amount) offset + amount else null
            LoadResult.Page(orders, prevKey, nextKey)
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, OrderStateItem>): Int = 0
}