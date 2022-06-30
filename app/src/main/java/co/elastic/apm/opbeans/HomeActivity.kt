package co.elastic.apm.opbeans

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.elastic.apm.opbeans.modules.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, HomeFragment::class.java, null)
                .commit()
        }
    }
}