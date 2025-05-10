package com.dzaky3022.asesment1.ui.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "deleted_results",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["uid"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = WaterResult::class,
            parentColumns = ["id"],
            childColumns = ["waterResultId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["uid"]),
        Index(value = ["waterResultId"])
    ]
)

data class DeletedResult(
    @PrimaryKey var id: String = UUID.randomUUID().toString().take(8),
    var uid: String? = null,
    var waterResultId: String? = null,
)