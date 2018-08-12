package com.psa.kblog.extensions

import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment

fun Fragment.navigateToBlogs() {
    startActivity(Intent()
            .apply {
                `package` = "com.psa.kblogs"
                data = Uri.parse("https://psa.com/blogs")
            })
}