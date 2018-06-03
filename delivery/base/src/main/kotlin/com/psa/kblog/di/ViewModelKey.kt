package com.psa.kblog.di

import android.arch.lifecycle.ViewModel
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
@kotlin.annotation.Retention(RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)
