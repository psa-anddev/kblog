@file:JvmName("FragmentExtensions")

package android.support.v4.app

import android.content.Context

fun Fragment.attach(context: Context) {
    mHost = (context as? FragmentActivity)?.HostCallbacks()
    onAttach(context)
}
