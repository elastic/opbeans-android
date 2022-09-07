package co.elastic.apm.opbeans.app

import android.app.Application
import co.elastic.apm.android.sdk.ElasticApmAgent
import co.elastic.apm.android.sdk.traces.connectivity.Connectivity
import co.elastic.apm.opbeans.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OpBeansApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val connectivity = Connectivity.create(BuildConfig.EXPORTER_URL).apply {
            if (BuildConfig.EXPORTER_AUTH_TOKEN.isNotEmpty()) {
                withAuthToken(BuildConfig.EXPORTER_AUTH_TOKEN)
            }
        }
        ElasticApmAgent.initialize(this, connectivity)
    }
}