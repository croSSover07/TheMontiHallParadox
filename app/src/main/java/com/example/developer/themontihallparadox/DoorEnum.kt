package com.example.developer.themontihallparadox

enum class DoorEnum {
    ONE {
        override fun getIdString(): Int =
                R.string.one
    },
    TWO {
        override fun getIdString(): Int =
                R.string.two
    },
    THREE {
        override fun getIdString(): Int =
                R.string.three
    };

    abstract fun getIdString():Int
}
