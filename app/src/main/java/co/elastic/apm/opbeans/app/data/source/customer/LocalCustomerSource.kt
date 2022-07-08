package co.elastic.apm.opbeans.app.data.source.customer

import co.elastic.apm.opbeans.app.data.local.AppDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCustomerSource @Inject constructor(appDatabase: AppDatabase) {

    private val customerDao by lazy { appDatabase.customerDao() }
}