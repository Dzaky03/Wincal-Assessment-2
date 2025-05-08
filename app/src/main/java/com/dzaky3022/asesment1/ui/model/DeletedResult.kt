package com.dzaky3022.asesment1.ui.model

import androidx.room.Entity

@Entity(tableName = "deleted_results")
data class DeletedResult(
    var id: String? = null,
    var uid: String? = null,
    var waterResultId: String? = null,
)