package com.psa.kblog.di

import com.nhaarman.mockito_kotlin.mock
import com.psa.kblog.blogs.create.CreateBlog
import com.psa.kblog.blogs.list.List
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.specs.ShouldSpec

class BlogsModuleSpec: ShouldSpec({
    should("return an interactor for creating blogs") {
        BlogsModule().provideCreateInteractor(mock {  }, mock {  }) should
                beInstanceOf(CreateBlog::class)
    }

    should("return an interactor for listing blogs") {
        BlogsModule().provideListInteractor(mock {  }, mock {  }) should
                beInstanceOf(List::class)
    }
})
