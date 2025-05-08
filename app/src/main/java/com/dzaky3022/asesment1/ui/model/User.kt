package com.dzaky3022.asesment1.ui.model

import androidx.room.Entity

@Entity(tableName = "users")
data class User(
    var id: String? = null,
    var nama: String? = null,
    var email: String? = null,
)