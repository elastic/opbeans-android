package co.elastic.apm.opbeans.app.tools

import android.content.Context
import co.elastic.apm.opbeans.app.OpBeansApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.io.InputStream

@GlideModule
@Excludes(OkHttpLibraryGlideModule::class)
class OpbeansGlideModule : AppGlideModule() {

    private val client: OkHttpClient

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltEntryPoint {
        fun okHttpClient(): OkHttpClient
    }

    init {
        val entryPoint = EntryPointAccessors.fromApplication(
            OpBeansApplication.INSTANCE,
            HiltEntryPoint::class.java
        )

        client = entryPoint.okHttpClient()
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }
}