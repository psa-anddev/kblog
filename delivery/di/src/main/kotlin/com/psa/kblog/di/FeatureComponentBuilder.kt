package com.psa.kblog.di

interface FeatureComponentBuilder<in T> {
    fun createComponent(component: T): FeatureAndroidInjector
}