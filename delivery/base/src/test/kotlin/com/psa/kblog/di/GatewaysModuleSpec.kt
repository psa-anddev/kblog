package com.psa.kblog.di

import com.nhaarman.mockito_kotlin.mock
import com.psa.kblog.gateways.RoomBlogsGateway
import com.psa.kblog.gateways.RoomUsersGateway
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.specs.ShouldSpec

class GatewaysModuleSpec: ShouldSpec({
    should("provide the right users gateway") {
        GatewaysModule().provideUsersGateway(mock {  }) should
                beInstanceOf(RoomUsersGateway::class)
    }

    should("provide the right blogs gateway") {
        GatewaysModule().provideBlogsGateway(mock {}) should
                beInstanceOf(RoomBlogsGateway::class)
    }
})
