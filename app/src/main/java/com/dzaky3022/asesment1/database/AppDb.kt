package com.dzaky3022.asesment1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dzaky3022.asesment1.ui.model.DeletedResult
import com.dzaky3022.asesment1.ui.model.User
import com.dzaky3022.asesment1.ui.model.WaterResult

@Database(entities = [User::class, WaterResult::class, DeletedResult::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun savedData(): SavedDataDao
    abstract fun deletedData(): DeletedDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getAppDb(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDatabase::class.java,
                    name = "wincal_db"
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}