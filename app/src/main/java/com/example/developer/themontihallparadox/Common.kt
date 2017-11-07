package com.example.developer.themontihallparadox

class Common {
    companion object {
        fun getEnumNameByIndex(number: Int): String {
            return when (number) {
                0 -> Enum.ONE.name
                1 -> Enum.TWO.name
                2 -> Enum.THREE.name
                else -> "error"
            }
        }
    }
}