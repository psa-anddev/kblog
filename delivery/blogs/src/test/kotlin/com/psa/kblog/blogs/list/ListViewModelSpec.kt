package com.psa.kblog.blogs.list

import android.arch.lifecycle.ViewModel
import com.nhaarman.mockito_kotlin.mock
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.specs.ShouldSpec
import javax.inject.Provider

class ListViewModelSpec: ShouldSpec({
    should("be a view model") {
        ListViewModel(Provider { mock<ListInput> { }}) should
                beInstanceOf(ViewModel::class)
    }
})
