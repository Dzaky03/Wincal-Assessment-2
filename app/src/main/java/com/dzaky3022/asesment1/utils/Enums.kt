package com.dzaky3022.asesment1.utils

class Enums {
    enum class ActivityLevel(val label: String, val value: Double) {
        Low("Sedentary", 35.0),
        Medium("Light Exercise", 40.0),
        High("Heavy Exercise", 45.0);
    }

    enum class TempUnit(val symbol: String) {
        Celsius("°C"),
        Fahrenheit("°F"),
        Kelvin("K");
    }

    enum class WeightUnit(val symbol: String) {
        Kilogram("kg"),
        Pound("lbs"),
    }

    enum class Gender(val value: String) {
        Male("Male"),
        Female("Female"),
    }

    enum class Direction {
        Horizontal,
        Vertical,
    }

    enum class WaterUnit {
        ml,
        oz,
        glasses,
    }
    enum class ScreenState {
        FirstScreen,
        SecondScreen,
        ThirdScreen,
    }
}