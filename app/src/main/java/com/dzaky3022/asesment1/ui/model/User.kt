package com.dzaky3022.asesment1.ui.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity(tableName = "users")
data class User(
    @PrimaryKey var id: String = UUID.randomUUID().toString().take(8),
    var nama: String? = null,
    var email: String? = null,
) {
    constructor() : this(UUID.randomUUID().toString().take(8))
}

data class UserWithResults(
    @Embedded val user: User,

    @Relation(
        parentColumn = "id",
        entityColumn = "uid"
    )
    val results: List<WaterResult>,

    @Relation(
        entity = DeletedResult::class,
        parentColumn = "id",
        entityColumn = "uid"
    )
    val deletedResults: List<DeletedResultWithWaterResult>
)