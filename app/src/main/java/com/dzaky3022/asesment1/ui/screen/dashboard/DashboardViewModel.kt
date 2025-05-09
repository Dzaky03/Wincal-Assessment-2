package com.dzaky3022.asesment1.ui.screen.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzaky3022.asesment1.R
import com.dzaky3022.asesment1.database.UserDao
import com.dzaky3022.asesment1.ui.model.User
import com.dzaky3022.asesment1.utils.DataStore
import com.dzaky3022.asesment1.utils.Enums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.UUID


class DashboardViewModel(
    private val localUser: StateFlow<User?>,
    private val userDao: UserDao,
    private val dataStore: DataStore,
) : ViewModel() {
    private val _isUserExisted = MutableStateFlow(false)
    val isUserExisted: StateFlow<Boolean> = _isUserExisted

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _authStatus = MutableStateFlow(Enums.ResponseStatus.Idle)
    val authStatus: StateFlow<Enums.ResponseStatus> = _authStatus

    init {
        viewModelScope.launch {
            localUser.collect {
                if (it != null) {
                    _user.value = it
                    _isUserExisted.value = true
                } else {
                    _isUserExisted.value = false
                }
            }
        }
    }

    fun onChange(newUser: User) {
        _user.value = newUser
    }

    fun signIn(context: Context) {
        _authStatus.value =
            Enums.ResponseStatus.Loading.apply { updateMessage(context.getString(R.string.waiting)) }
        viewModelScope.launch(Dispatchers.IO) {
            _user.value?.let {
                val response = userDao.getUserByEmail(it.email ?: "")
                if (response != null) {
                    dataStore.saveEmail(it.email ?: "")
                    if (dataStore.emailFlow.firstOrNull() != null)
                        _authStatus.value = Enums.ResponseStatus.Success.apply {
                            updateMessage(
                                context.getString(
                                    R.string.login_success
                                )
                            )
                        }
                    else
                        _authStatus.value = Enums.ResponseStatus.Failed.apply {
                            updateMessage(
                                context.getString(R.string.login_failed)
                            )
                        }
                } else {
                    val finalUser = it.copy(id = UUID.randomUUID().toString().take(8))
                    val responseInsert = userDao.insertUser(finalUser)
                    dataStore.saveEmail(finalUser.email ?: "")
                    if (responseInsert != null && dataStore.emailFlow.firstOrNull() != null)
                        _authStatus.value =
                            Enums.ResponseStatus.Success.apply { updateMessage(context.getString(R.string.register_success)) }
                    else
                        _authStatus.value =
                            Enums.ResponseStatus.Failed.apply { updateMessage(context.getString(R.string.register_failed)) }
                    Log.d("DashboardVM", "finalUser: $finalUser")
                }
            }
        }
    }

    fun reset() {
        _authStatus.value = Enums.ResponseStatus.Idle
    }

    override fun onCleared() {
        super.onCleared()
        _authStatus.value = Enums.ResponseStatus.Idle
    }
}