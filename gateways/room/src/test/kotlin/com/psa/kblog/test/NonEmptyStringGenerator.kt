package com.psa.kblog.test

import io.kotlintest.properties.Gen

class NonEmptyStringGenerator: Gen<String> {
    override fun constants(): Iterable<String> =
            emptyList()

    override fun random(): Sequence<String> =
            Gen.string().random().filter { it.isNotBlank() }
}