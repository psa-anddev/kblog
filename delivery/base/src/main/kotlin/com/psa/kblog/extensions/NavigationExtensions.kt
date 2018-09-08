package com.psa.kblog.extensions

import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment

fun Fragment.navigateToBlogs() {
    startActivity(Intent()
            .apply {
                this.`package` = "com.psa.kblog.app"
                data = Uri.parse("kblogs://blogs")
            })
}

fun Fragment.navigateToUsers() {
    startActivity(Intent()
            .apply {
                this.`package` = "com.psa.kblog.app"
                data = Uri.parse("kblogs://main")
            })
}
