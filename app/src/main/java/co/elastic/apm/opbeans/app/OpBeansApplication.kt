/* 
Licensed to Elasticsearch B.V. under one or more contributor
license agreements. See the NOTICE file distributed with
this work for additional information regarding copyright
ownership. Elasticsearch B.V. licenses this file to you under
the Apache License, Version 2.0 (the "License"); you may
not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License. 
*/
package co.elastic.apm.opbeans.app

import android.app.Application
import co.elastic.otel.android.ElasticApmAgent
import co.elastic.otel.android.api.ElasticOtelAgent
import co.elastic.otel.android.connectivity.Authentication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OpBeansApplication : Application() {

    companion object {
        lateinit var INSTANCE: OpBeansApplication
            private set
    }

    lateinit var agent: ElasticOtelAgent

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        // Check out the docs for more details: https://www.elastic.co/guide/en/apm/agent/android/current/index.html
        agent = ElasticApmAgent.builder(this)
            .setServiceName("opbeans-android")
            .setExportUrl("my_export_url")
            .setExportAuthentication(Authentication.ApiKey("my_api_key"))
            .build()
    }
}