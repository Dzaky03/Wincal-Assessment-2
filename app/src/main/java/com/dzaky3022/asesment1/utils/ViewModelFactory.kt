package com.dzaky3022.asesment1.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dzaky3022.asesment1.database.UserDao
import com.dzaky3022.asesment1.database.WaterResultDao
import com.dzaky3022.asesment1.ui.model.User
import com.dzaky3022.asesment1.ui.screen.dashboard.DashboardViewModel
import com.dzaky3022.asesment1.ui.screen.deleted_list.DeletedListViewModel
import com.dzaky3022.asesment1.ui.screen.form.FormViewModel
import com.dzaky3022.asesment1.ui.screen.list.ListViewModel
import kotlinx.coroutines.flow.StateFlow

class ViewModelFactory(
    private val waterResultId: String? = null,
    private val localUser: StateFlow<User?>? = null,
    private val dataStore: DataStore? = null,
    private val userDao: UserDao? = null,
    private val waterResultDao: WaterResultDao? = null,
    private val useFab: Boolean? = null,
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
        } else if (modelClass.isAssignableFrom(FormViewModel::class.java)) {
            return FormViewModel(
                localUser!!,
                waterResultId,
                waterResultDao!!,
                useFab,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}