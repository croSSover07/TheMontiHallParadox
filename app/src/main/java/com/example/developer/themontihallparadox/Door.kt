package com.example.developer.themontihallparadox

import android.support.annotation.IdRes
import android.support.annotation.StringRes

// TODO: не нужно дублировать в имени Enum, ты же не пишешь MainActivityClass.
// https://kotlinlang.org/docs/reference/enum-classes.html
enum class Door {
    ONE {
        override fun getViewId() = R.id.imageView_first_door
        override fun getTitleResId() = R.string.one
    },
    TWO {
        override fun getViewId() = R.id.imageView_second_door
        override fun getTitleResId() = R.string.two
    },
    THREE {
        override fun getViewId() = R.id.imageView_third_door
        override fun getTitleResId() = R.string.three
    };

    companion object {
        fun doorForViewWithId(id: Int) = values().firstOrNull { it.getViewId() == id }
    }

//    TODO: Naming!
//    abstract fun getIdString(): Int
    @StringRes
    abstract fun getTitleResId(): Int

    @IdRes
    abstract fun getViewId(): Int
}
