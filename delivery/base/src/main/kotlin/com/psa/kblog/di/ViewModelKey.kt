package com.psa.kblog.di

import android.arch.lifecycle.ViewModel
import dagger.MapKey
import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
@kotlin.annotation.Retention(RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
