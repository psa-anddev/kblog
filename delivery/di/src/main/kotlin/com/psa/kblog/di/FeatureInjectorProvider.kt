package com.psa.kblog.di

interface FeatureInjectorProvider {
    fun checkForNewInjectors(): List<FeatureAndroidInjector>
}