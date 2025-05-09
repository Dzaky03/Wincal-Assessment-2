package com.dzaky3022.asesment1.ui.screen.form

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzaky3022.asesment1.R
import com.dzaky3022.asesment1.database.UserDao
import com.dzaky3022.asesment1.database.WaterResultDao
import com.dzaky3022.asesment1.ui.model.User
import com.dzaky3022.asesment1.ui.model.WaterResult
import com.dzaky3022.asesment1.utils.Enums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FormViewModel(
    private val localUser: User? = null,
    private val userDao: UserDao,
    private val waterResultDao: WaterResultDao,
) : ViewModel() {

    private val _insertStatus = MutableStateFlow(Enums.ResponseStatus.Idle)
    val insertStatus: StateFlow<Enums.ResponseStatus> = _insertStatus

    fun insert(waterResult: WaterResult, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = waterResultDao.insertData(waterResult)
            if (response != null && response > 0L)
                _insertStatus.value = Enums.ResponseStatus.Success.apply {
                    updateMessage(
                        context.getString(
                            R.string.insert_data_success
                        )
                    )
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
}