package com.dzaky3022.asesment1.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dzaky3022.asesment1.database.UserDao
import com.dzaky3022.asesment1.database.WaterResultDao
import com.dzaky3022.asesment1.ui.model.User
import com.dzaky3022.asesment1.ui.screen.dashboard.DashboardViewModel
import com.dzaky3022.asesment1.ui.screen.deleted_list.DeletedListViewModel
import com.dzaky3022.asesment1.ui.screen.deleted_list.ListViewModel

class ViewModelFactory(
    private val waterResultId: String? = null,
    private val localUser: User? = null,
    private val dataStore: DataStore? = null,
    private val userDao: UserDao? = null,
    private val waterResultDao: WaterResultDao? = null,
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(localUser!!, userDao!!, dataStore!!) as T
        } else if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(
                localUser!!,
                userDao!!,
                waterResultDao!!,
                dataStore!!,
            ) as T
        } else if (modelClass.isAssignableFrom(DeletedListViewModel::class.java)) {
            return DeletedListViewModel(
                localUser!!,
                userDao!!,
                waterResultDao!!,
                dataStore!!,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}