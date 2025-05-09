package com.dzaky3022.asesment1.utils

import android.content.Context
import androidx.annotation.StringRes
import com.dzaky3022.asesment1.R

class Enums {
    enum class ActivityLevel(@StringRes val labelResId: Int, val value: Double) {
        Low(R.string.low, 35.0),
        Medium(R.string.medium, 40.0),
        High(R.string.high, 45.0);

        fun getLabel(context: Context): String = context.getString(labelResId)
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
    enum class Gender(@StringRes val labelResId: Int) {
        Male(R.string.male),
        Female(R.string.female);

        fun getLabel(context: Context): String = context.getString(labelResId)
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
    enum class OrientationView {
        List,
        Grid,
    }
    enum class DataStatus {
        Deleted,
        Available,
    }
    enum class ResponseStatus(var message: String? = null) {
        Success,
        Failed,
        Idle,
        Loading;

        fun updateMessage(message: String? = null) {
            this.message = message
        }
    }
}