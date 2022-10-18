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
package co.elastic.apm.opbeans.app.tools

import android.app.Activity
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.elastic.apm.opbeans.BuildConfig
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders

fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun RequestManager.loadOpbeans(imageUrl: String): RequestBuilder<Drawable> {
    val url = if (BuildConfig.OPBEANS_AUTH_TOKEN.isNotEmpty()) {
        GlideUrl(
            imageUrl,
            LazyHeaders.Builder()
                .addHeader("Authorization", "Basic ${BuildConfig.OPBEANS_AUTH_TOKEN}").build()
        )
    } else {
        GlideUrl(imageUrl)
    }
    return load(url)
}