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