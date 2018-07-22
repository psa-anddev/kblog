package com.psa.kblog.di

import android.content.Context
import android.content.pm.PackageManager.GET_META_DATA
import javax.inject.Inject

class ManifestFeatureInjectorProvider<T>
    @Inject constructor(context: Context,
                        private val component: T)
    : FeatureInjectorProvider {
    private val manifestInjectors: List<String>
    private val seenInjectors = mutableListOf<String>()

    init {
        val metaData = context.packageManager
                .getApplicationInfo(context.packageName, GET_META_DATA)
                .metaData
        manifestInjectors = metaData
                .keySet()
                .filter { it.startsWith("feature.injector") }
                .map { metaData.getString(it) }

    }
    override fun checkForNewInjectors(): List<FeatureAndroidInjector> {
        return manifestInjectors.filterNot { seenInjectors.contains(it) }
                .mapNotNull { constructComponent(it) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun constructComponent(name: String): FeatureAndroidInjector? {
        return try {
            val clazz = Class.forName(name)
            seenInjectors.add(name)
            (clazz.newInstance() as FeatureComponentBuilder<T>)
                    .createComponent(component)
        } catch (ex: ClassNotFoundException) {
            null
        } catch (ex: InstantiationException) {
            throw RuntimeException(
                     "Feature component builder $name must have a public no args constructor",
                    ex)
        } catch (ex: IllegalAccessException) {
            throw RuntimeException(
                    "Feature component builder $name must have a public no args constructor",
                    ex)
        } catch (ex: ClassCastException) {
            throw RuntimeException(ex)
        }
    }
}