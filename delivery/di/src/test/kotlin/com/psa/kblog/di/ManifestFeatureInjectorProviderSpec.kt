package com.psa.kblog.di

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageItemInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.support.v4.app.Fragment
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import dagger.android.DispatchingAndroidInjector
import io.kotlintest.matchers.beInstanceOf
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.ShouldSpec
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table

class ManifestFeatureInjectorProviderSpec : ShouldSpec({
    should("be a feature injector provider") {
        val packageName = "com.psa.kblog"
        val applicationInfo = MockApplicationInfo().apply {
            setPackageName("com.psa")
            metaData = mock { }
        }

        val packageManager = mock<PackageManager> {
            on { getApplicationInfo(packageName, GET_META_DATA) } doReturn
                    applicationInfo
        }
        val context = mock<Context> {
            on { this.packageManager } doReturn packageManager
            on { this.packageName } doReturn packageName
        }
        ManifestFeatureInjectorProvider<Any>(context, mock { }) should
                beInstanceOf(FeatureInjectorProvider::class)
    }

    should("check for new injectors") {
        val values = table(headers("package name",
                "metadata",
                "injectors"),
                row("com.google.maps",
                        emptyMap(),
                        0),
                row("com.airbnb.app",
                        mapOf<String, Any>(
                                "feature.injector.feature1" to
                                        "com.psa.kblog.di.MockFeatureAndroidInjectorBuilder",
                                "feature.injector.feature2" to
                                        "com.psa.kblog.di.MockFeatureAndroidInjectorBuilder",
                                "feature.injector.feature3" to
                                        "com.psa.kblog.di.MockFeatureAndroidInjectorBuilder"),
                        3),
                row("com.airbnb.app",
                        mapOf<String, Any>(
                                "feature.injector.feature1" to
                                        "com.psa.kblog.di.MockFeatureAndroidInjectorBuilder",
                                "feature.injector.feature2" to
                                        "com.psa.kblog.di.MockFeatureAndroidInjectorBuilder",
                                "feature.injector.feature3" to
                                        "com.psa.kblog.di.MockFeatureAndroidInjectorBuilder2"),
                        2))

        forAll(values) { packageName: String, metadata: Map<String, Any>, injectorSize: Int ->
            val applicationInfo = MockApplicationInfo().apply {
                setPackageName(packageName)
                (this as ApplicationInfo).metaData = mock {
                    on { keySet() } doReturn metadata.keys
                    metadata.forEach {
                        on { getString(it.key) } doReturn it.value as String
                    }
                }
            }
            val packageManager = mock<PackageManager> {
                on { getApplicationInfo(packageName, GET_META_DATA) } doReturn
                        applicationInfo
            }
            val context = mock<Context> {
                on { this.packageManager } doReturn packageManager
                on { this.packageName } doReturn packageName
            }

            val provider = ManifestFeatureInjectorProvider<Any>(context, mock { })

            provider.checkForNewInjectors().size shouldBe injectorSize
        }
    }

    should("handle instantiation exception") {
        val packageName = "com.psa.kblog.di"
        val metadata = mapOf<String, Any>(
                "feature.injector" to "com.psa.kblog.di.OneParameterFeatureAndroidInjectorBuilder"
        )
        val applicationInfo = MockApplicationInfo().apply {
            setPackageName(packageName)
            (this as ApplicationInfo).metaData = mock {
                on { keySet() } doReturn metadata.keys
                metadata.forEach {
                    on { getString(it.key) } doReturn it.value as String
                }
            }
        }
        val packageManager = mock<PackageManager> {
            on { getApplicationInfo(packageName, GET_META_DATA) } doReturn
                    applicationInfo
        }
        val context = mock<Context> {
            on { this.packageManager } doReturn packageManager
            on { this.packageName } doReturn packageName
        }

        val provider = ManifestFeatureInjectorProvider<Any>(context, mock { })

        val exception = shouldThrow<RuntimeException> {
            provider.checkForNewInjectors()
        }

        exception.message shouldBe "Feature component builder " +
                "com.psa.kblog.di.OneParameterFeatureAndroidInjectorBuilder must have a " +
                "public no args constructor"
        exception.cause shouldNotBe null
        (exception.cause as Throwable) should beInstanceOf(InstantiationException::class)
    }


    should("handle illegal access exception") {
        val packageName = "com.psa.kblog.di"
        val metadata = mapOf<String, Any>(
                "feature.injector" to "com.psa.kblog.di.PrivateFeatureAndroidInjectorBuilder"
        )
        val applicationInfo = MockApplicationInfo().apply {
            setPackageName(packageName)
            (this as ApplicationInfo).metaData = mock {
                on { keySet() } doReturn metadata.keys
                metadata.forEach {
                    on { getString(it.key) } doReturn it.value as String
                }
            }
        }
        val packageManager = mock<PackageManager> {
            on { getApplicationInfo(packageName, GET_META_DATA) } doReturn
                    applicationInfo
        }
        val context = mock<Context> {
            on { this.packageManager } doReturn packageManager
            on { this.packageName } doReturn packageName
        }

        val provider = ManifestFeatureInjectorProvider<Any>(context, mock { })

        val exception = shouldThrow<RuntimeException> {
            provider.checkForNewInjectors()
        }

        exception.message shouldBe "Feature component builder " +
                "com.psa.kblog.di.PrivateFeatureAndroidInjectorBuilder must have a " +
                "public no args constructor"
        exception.cause shouldNotBe null
        (exception.cause as Throwable) should beInstanceOf(IllegalAccessException::class)
    }


    should("handle class cast exception") {
        val packageName = "com.psa.kblog.di"
        val metadata = mapOf<String, Any>(
                "feature.injector" to "com.psa.kblog.di.StringFeatureAndroidInjectorBuilder"
        )
        val applicationInfo = MockApplicationInfo().apply {
            setPackageName(packageName)
            (this as ApplicationInfo).metaData = mock {
                on { keySet() } doReturn metadata.keys
                metadata.forEach {
                    on { getString(it.key) } doReturn it.value as String
                }
            }
        }
        val packageManager = mock<PackageManager> {
            on { getApplicationInfo(packageName, GET_META_DATA) } doReturn
                    applicationInfo
        }
        val context = mock<Context> {
            on { this.packageManager } doReturn packageManager
            on { this.packageName } doReturn packageName
        }

        val provider = ManifestFeatureInjectorProvider<MockFeatureAndroidInjectorBuilder>(context, mock { })

        val exception = shouldThrow<RuntimeException> {
            provider.checkForNewInjectors()
        }

        exception.cause shouldNotBe null
        (exception.cause as Throwable) should beInstanceOf(ClassCastException::class)
    }
})

class MockApplicationInfo : ApplicationInfo() {
    fun setPackageName(packageName: String) {
        (this as PackageItemInfo).packageName = packageName
    }
}

@Suppress("unused")
internal open class MockFeatureAndroidInjectorBuilder : FeatureComponentBuilder<Any> {
    override fun createComponent(component: Any): FeatureAndroidInjector {
        return object : FeatureAndroidInjector {
            override val activityInjector: DispatchingAndroidInjector<Activity>
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
            override val supportFragmentInjector: DispatchingAndroidInjector<Fragment>
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        }
    }
}


@Suppress("unused")
internal class OneParameterFeatureAndroidInjectorBuilder(val id: Int) : FeatureComponentBuilder<Any> {
    override fun createComponent(component: Any): FeatureAndroidInjector {
        return object : FeatureAndroidInjector {
            override val activityInjector: DispatchingAndroidInjector<Activity>
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
            override val supportFragmentInjector: DispatchingAndroidInjector<Fragment>
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        }
    }
}

@Suppress("unused")
internal class PrivateFeatureAndroidInjectorBuilder private constructor(): FeatureComponentBuilder<Any> {
    override fun createComponent(component: Any): FeatureAndroidInjector {
        return object : FeatureAndroidInjector {
            override val activityInjector: DispatchingAndroidInjector<Activity>
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
            override val supportFragmentInjector: DispatchingAndroidInjector<Fragment>
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        }
    }
}

@Suppress("unused")
internal class StringFeatureAndroidInjectorBuilder: FeatureComponentBuilder<OneParameterFeatureAndroidInjectorBuilder> {
    override fun createComponent(component: OneParameterFeatureAndroidInjectorBuilder): FeatureAndroidInjector {
        return object : FeatureAndroidInjector {
            override val activityInjector: DispatchingAndroidInjector<Activity>
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
            override val supportFragmentInjector: DispatchingAndroidInjector<Fragment>
                get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        }
    }
}
