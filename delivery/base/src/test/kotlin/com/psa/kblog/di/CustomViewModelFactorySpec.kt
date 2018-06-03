package com.psa.kblog.di

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.nhaarman.mockito_kotlin.mock
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class CustomViewModelFactorySpec: ShouldSpec({
    should("be a view model provider factory") {
        CustomViewModelFactory(mutableMapOf()) should
                beInstanceOf(ViewModelProvider.Factory::class)
    }
    should("return null if the view model is not included in the factory") {
        CustomViewModelFactory(mutableMapOf()).create(ViewModel::class.java) shouldBe null
    }

    should("return the right view model if the view model is provided") {
        val expected = mock<AndroidViewModel> {  }
        CustomViewModelFactory(
                mutableMapOf(
                        AndroidViewModel::class.java to Provider<AndroidViewModel> { expected })
                        as MutableMap<Class<out ViewModel>, Provider<ViewModel>>)
                .create(AndroidViewModel::class.java) shouldBe expected
    }
})
