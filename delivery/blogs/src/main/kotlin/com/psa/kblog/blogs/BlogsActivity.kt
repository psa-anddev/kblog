package com.psa.kblog.blogs

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController

class BlogsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blogs_activity)
    }

    override fun onSupportNavigateUp(): Boolean =
            findNavController(R.id.host).navigateUp()
}
