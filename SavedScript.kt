package com.xtremeclicker.pro.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xtremeclicker.pro.model.Action

@Entity(tableName = "saved_scripts")
data class SavedScript(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val actionsJson: String, // JSON das ações gravadas
    val thumbnailPath: String? = null // opcional: mini-print da tela
)
