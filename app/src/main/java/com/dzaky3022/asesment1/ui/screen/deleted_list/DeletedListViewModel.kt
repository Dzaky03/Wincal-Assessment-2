package com.dzaky3022.asesment1.ui.screen.deleted_list

import android.content.Context
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

class DeletedListViewModel(
    private val localUser: User? = null,
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

    private val _logOutStatus = MutableStateFlow(Enums.ResponseStatus.Idle)
    val logOutStatus: StateFlow<Enums.ResponseStatus> = _logOutStatus

    private val _updateStatus = MutableStateFlow(Enums.ResponseStatus.Idle)
    val updateStatus: StateFlow<Enums.ResponseStatus> = _updateStatus

    init {
        getList()
    }

    fun getList() {
        _loadStatus.value = Enums.ResponseStatus.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val response = userDao.getUserWithResults(localUser?.id ?: "")
            _listData.value = response.results
            _userData.value = response.user
            _loadStatus.value = Enums.ResponseStatus.Idle
        }
    }

    fun deleteDataPermanent(waterResultId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            waterResultDao.deletePermanent(waterResultId)
        }
        getList()
    }

    fun updateData(waterResult: WaterResult, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = waterResultDao.update(waterResult)
            if (response > 0)
                _updateStatus.value = Enums.ResponseStatus.Success.apply {
                    updateMessage(
                        context.getString(
                            R.string.update_data_success
                        )
                    )
                }
            else
                _updateStatus.value = Enums.ResponseStatus.Failed.apply {
                    updateMessage(
                        context.getString(R.string.update_data_failed)
                    )
                }
        }
        getList()
    }

    fun logout(context: Context) {
        _logOutStatus.value = Enums.ResponseStatus.Loading
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
}