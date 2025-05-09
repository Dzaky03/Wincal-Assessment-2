package com.dzaky3022.asesment1.ui.screen.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzaky3022.asesment1.database.UserDao
import com.dzaky3022.asesment1.ui.model.User
import com.dzaky3022.asesment1.utils.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID


class DashboardViewModel(
    private val localUser: User? = null,
    private val userDao: UserDao,
    private val dataStore: DataStore,
) : ViewModel() {
    private val _isUserExisted = MutableStateFlow(false)
    val isUserExisted: StateFlow<Boolean> = _isUserExisted

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        _user.value = localUser
        _isUserExisted.value = localUser != null
    }

    fun onChange(newUser: User) {
        _user.value = newUser
    }

    fun saveUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _user.value?.let {
                val finalUser = it.copy(id = UUID.randomUUID().toString().take(8))
                userDao.insertUser(finalUser)
                dataStore.saveEmail(finalUser.email ?: "")
                Log.d("DashboardVM", "finalUser: $finalUser")
            }
        }
    }
}