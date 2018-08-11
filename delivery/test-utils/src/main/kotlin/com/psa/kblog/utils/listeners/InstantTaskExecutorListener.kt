package com.psa.kblog.utils.listeners

import android.annotation.SuppressLint
import android.arch.core.executor.ArchTaskExecutor
import android.arch.core.executor.TaskExecutor
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.extensions.TestListener


class InstantTaskExecutorListener: TestListener {
    @SuppressLint("RestrictedApi")
    override fun beforeSpec(description: Description, spec: Spec) {
        super.beforeSpec(description, spec)
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean = true

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })
    }

    @SuppressLint("RestrictedApi")
    override fun afterSpec(description: Description, spec: Spec) {
        ArchTaskExecutor.getInstance().setDelegate(null)
        super.afterSpec(description, spec)
    }
}