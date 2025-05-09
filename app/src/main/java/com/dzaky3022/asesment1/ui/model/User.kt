package com.dzaky3022.asesment1.ui.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class User(
    @PrimaryKey var id: String = UUID.randomUUID().toString().take(8),
    var nama: String? = null,
    var email: String? = null,
) {
    constructor() : this(UUID.randomUUID().toString().take(8))
}