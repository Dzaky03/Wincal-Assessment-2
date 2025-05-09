package com.dzaky3022.asesment1.ui.screen.list

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzaky3022.asesment1.R
import com.dzaky3022.asesment1.database.UserDao
import com.dzaky3022.asesment1.database.WaterResultDao
import com.dzaky3022.asesment1.ui.model.DeletedResult
import com.dzaky3022.asesment1.ui.model.User
import com.dzaky3022.asesment1.ui.model.WaterResult
import com.dzaky3022.asesment1.utils.DataStore
import com.dzaky3022.asesment1.utils.Enums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ListViewModel(
    private val localUser: StateFlow<User?>,
    private val userDao: UserDao,
    private val waterResultDao: WaterResultDao,
    private val dataStore: DataStore,
) : ViewModel() {

    private val _listData = MutableStateFlow<List<WaterResult>?>(null)
    val listData: StateFlow<List<WaterResult>?> = _listData

    private val _userData = MutableStateFlow<User?>(null)
    val userData: StateFlow<User?> = _userData

    private val _loadStatus = MutableStateFlow(Enums.ResponseStatus.Idle)
    val loadStatus: StateFlow<Enums.ResponseStatus> = _loadStatus

    private val _deleteStatus = MutableStateFlow(Enums.ResponseStatus.Idle)
    val deleteStatus: StateFlow<Enums.ResponseStatus> = _deleteStatus

    private val _logOutStatus = MutableStateFlow(Enums.ResponseStatus.Idle)
    val logOutStatus: StateFlow<Enums.ResponseStatus> = _logOutStatus

    private val _deleteAccountStatus = MutableStateFlow(Enums.ResponseStatus.Idle)
    val deleteAccountStatus: StateFlow<Enums.ResponseStatus> = _deleteAccountStatus

    private val _orientationView = MutableStateFlow(Enums.OrientationView.List)
    val orientationView: StateFlow<Enums.OrientationView> = _orientationView

    init {
        viewModelScope.launch {
            localUser.collect {
                _userData.value = it
            }
        }
        getList()
        getOrientationView()
    }

    fun getList() {
        _userData.value.let {
            _loadStatus.value = Enums.ResponseStatus.Loading
            viewModelScope.launch(Dispatchers.IO) {
                val response = waterResultDao.getDataByUser(
                    _userData.value?.id ?: "",
                    status = Enums.DataStatus.Available
                )
                _listData.value = response
                _loadStatus.value = Enums.ResponseStatus.Idle
                Log.d("ListVM", "list: $response")
            }
        }
    }

    fun deleteData(waterResultId: String, context: Context) {
        _deleteStatus.value = Enums.ResponseStatus.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response1 = waterResultDao.deleteById(waterResultId)
            val response2 = waterResultDao.insertDeletedData(
                DeletedResult(
                    uid = _userData.value?.id ?: "",
                    waterResultId = waterResultId
                )
            )
            if (response1 > 0 && response2 != null)
                _deleteStatus.value =
                    Enums.ResponseStatus.Success.apply { updateMessage(context.getString(R.string.delete_data_success)) }
            else
                _deleteStatus.value =
                    Enums.ResponseStatus.Failed.apply { updateMessage(context.getString(R.string.delete_data_failed)) }
            getList()
        }
    }

    fun logout(context: Context) {
        _logOutStatus.value =
            Enums.ResponseStatus.Loading.apply { updateMessage(context.getString(R.string.waiting)) }
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.clearEmail()
            if (dataStore.emailFlow.firstOrNull().isNullOrEmpty())
                _logOutStatus.value = Enums.ResponseStatus.Success.apply {
                    updateMessage(
                        context.getString(
                            R.string.logout_success
                        )
                    )
                }
            else
                _logOutStatus.value = Enums.ResponseStatus.Failed.apply {
                    updateMessage(
                        context.getString(
                            R.string.logout_failed
                        )
                    )
                }

        }
    }

    fun deleteAccount(context: Context) {
        _deleteAccountStatus.value =
            Enums.ResponseStatus.Loading.apply { updateMessage(context.getString(R.string.waiting)) }
        viewModelScope.launch(Dispatchers.IO) {
            val response = userDao.deleteUserById(_userData.value?.id ?: "")
            dataStore.clearEmail()
            if (response > 0 && dataStore.emailFlow.firstOrNull().isNullOrEmpty())
                _deleteAccountStatus.value = Enums.ResponseStatus.Success.apply {
                    updateMessage(
                        context.getString(R.string.delete_account_success),
                    )
                }
            else
                _deleteAccountStatus.value = Enums.ResponseStatus.Failed.apply {
                    updateMessage(
                        context.getString(R.string.delete_account_failed),
                    )
                }

        }
    }

    private fun getOrientationView() {
        viewModelScope.launch {
            dataStore.layoutFlow.collect {
                _orientationView.value =
                    if (it) Enums.OrientationView.List else Enums.OrientationView.Grid
            }
        }
    }

    fun changeOrientationView(orientationView: Enums.OrientationView) {
        viewModelScope.launch {
            dataStore.saveLayout(orientationView)
            getOrientationView()
        }
    }

    fun reset() {
        _logOutStatus.value = Enums.ResponseStatus.Idle
        _deleteAccountStatus.value = Enums.ResponseStatus.Idle
        _deleteStatus.value = Enums.ResponseStatus.Idle
    }

    override fun onCleared() {
        super.onCleared()
        _logOutStatus.value = Enums.ResponseStatus.Idle
        _deleteAccountStatus.value = Enums.ResponseStatus.Idle
        _deleteStatus.value = Enums.ResponseStatus.Idle
        _loadStatus.value = Enums.ResponseStatus.Idle
    }
}