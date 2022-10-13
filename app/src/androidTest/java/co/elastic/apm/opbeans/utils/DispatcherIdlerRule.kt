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
package co.elastic.apm.opbeans.utils

import co.elastic.apm.opbeans.app.tools.MyDispatchers
import kotlinx.coroutines.Dispatchers
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class DispatcherIdlerRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                val espressoTrackedDispatcherIO = EspressoTrackedDispatcher(Dispatchers.IO)
                val espressoTrackedDispatcherDefault =
                    EspressoTrackedDispatcher(Dispatchers.Default)
                MyDispatchers.IO = espressoTrackedDispatcherIO
                MyDispatchers.Default = espressoTrackedDispatcherDefault
                try {
                    base.evaluate()
                } finally {
                    espressoTrackedDispatcherIO.cleanUp()
                    espressoTrackedDispatcherDefault.cleanUp()
                    MyDispatchers.resetAll()
                }
            }
        }
}