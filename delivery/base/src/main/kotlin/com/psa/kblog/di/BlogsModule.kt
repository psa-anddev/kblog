package com.psa.kblog.di

import com.psa.kblog.blogs.create.CreateBlog
import com.psa.kblog.blogs.create.CreateBlogInput
import com.psa.kblog.blogs.list.List
import com.psa.kblog.blogs.list.ListInput
import com.psa.kblog.gateways.BlogsGateway
import com.psa.kblog.gateways.UsersGateway
import dagger.Module
import dagger.Provides

@Module
class BlogsModule {
    @Provides
    fun provideCreateInteractor(usersGateway: UsersGateway,
                                blogsGateway: BlogsGateway)
            : CreateBlogInput =
            CreateBlog(usersGateway, blogsGateway)

    @Provides
    fun provideListInteractor(usersGateway: UsersGateway,
                              blogsGateway: BlogsGateway): ListInput =
            List(usersGateway, blogsGateway)
}