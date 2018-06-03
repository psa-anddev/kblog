package com.psa.kblog.di

import android.content.Context
import com.psa.kblog.gateways.BlogsGateway
import com.psa.kblog.gateways.RoomBlogsGateway
import com.psa.kblog.gateways.RoomUsersGateway
import com.psa.kblog.gateways.UsersGateway
import dagger.Module
import dagger.Provides

@Module
class GatewaysModule {
    @Provides
    fun provideUsersGateway(context: Context) : UsersGateway =
            RoomUsersGateway(context)

    @Provides
    fun provideBlogsGateway(context: Context): BlogsGateway =
            RoomBlogsGateway(context)
}