package com.dzaky3022.asesment1.ui.screen.form

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzaky3022.asesment1.R
import com.dzaky3022.asesment1.database.WaterResultDao
import com.dzaky3022.asesment1.ui.model.User
import com.dzaky3022.asesment1.ui.model.WaterResult
import com.dzaky3022.asesment1.utils.Enums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.threeten.bp.Instant

class FormViewModel(
    private val localUser: StateFlow<User?>,
    private val waterResultId: String? = null,
    private val waterResultDao: WaterResultDao,
    private val useFab: Boolean? = false,
) : ViewModel() {

    private val _isUpdate = MutableStateFlow(false)
    val isUpdate: StateFlow<Boolean> = _isUpdate

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _insertStatus = MutableStateFlow(Enums.ResponseStatus.Idle)
    val insertStatus: StateFlow<Enums.ResponseStatus> = _insertStatus

    private val _updateStatus = MutableStateFlow(Enums.ResponseStatus.Idle)
    val updateStatus: StateFlow<Enums.ResponseStatus> = _updateStatus

    private val _data = MutableStateFlow<WaterResult?>(null)
    val data: StateFlow<WaterResult?> = _data

    private val _isDataExist = MutableStateFlow(false)
    val isDataExist: StateFlow<Boolean> = _isDataExist

    private val _useFAB = MutableStateFlow(false)
    val useFAB: StateFlow<Boolean> = _useFAB

    init {
        viewModelScope.launch {
            localUser.collect {
                _user.value = it
                checkIfUserHasData()
            }
        }
        if (!waterResultId.isNullOrEmpty()) {
            getData()
            _isUpdate.value = true
        }
        _useFAB.value = useFab!!
    }

    fun insert(waterResult: WaterResult, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = waterResultDao.insertData(waterResult.copy(uid = _user.value?.id ?: "", createdAt = Instant.now(), updatedAt = Instant.now()))
            if (response != null && response > 0L)
                _insertStatus.value = Enums.ResponseStatus.Success.apply {
                    updateMessage(
                        context.getString(
                            R.string.insert_data_success
                        )
                    )
                    Log.d("DashboardVM", "success inserted data: $waterResult")
                }
            else
                _insertStatus.value = Enums.ResponseStatus.Failed.apply {
                    updateMessage(
                        context.getString(
                            R.string.insert_data_failed
                        )
                    )
                }
        }
    }


    fun updateData(waterResult: WaterResult, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = waterResultDao.update(waterResult.copy(id = _data.value?.id ?: "", uid = _user.value?.id ?: "", updatedAt = Instant.now()))
            if (response > 0)
                _updateStatus.value = Enums.ResponseStatus.Success.apply {
                    updateMessage(
                        context.getString(
                            R.string.update_data_success
                        )
                    )
                    Log.d("FormVM", "success update data: $waterResult")
                }
            else
                _updateStatus.value = Enums.ResponseStatus.Failed.apply {
                    updateMessage(
                        context.getString(R.string.update_data_failed)
                    )
                    Log.d("FormVM", "failed update data: $waterResult")
                }
        }
    }

    private fun checkIfUserHasData() {
        Log.d("FormVM", "function: CheckIFHasData, localUser: $localUser")
        viewModelScope.launch(Dispatchers.IO) {
            if (_user.value != null) {
                val response = waterResultDao.getDataByUser(_user.value!!.id)
                Log.d("FormVM", "function: CheckIFHasData, response: $response")
                _isDataExist.value = !response.isNullOrEmpty()
            } else
                _isDataExist.value = false
        }
    }

    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            _data.value = waterResultId?.let { waterResultDao.getDataById(it) }
            Log.d("FormVM", "fetched data: ${_data.value}")
        }
    }

    fun reset() {
        _insertStatus.value = Enums.ResponseStatus.Idle
        _updateStatus.value = Enums.ResponseStatus.Idle
    }
}