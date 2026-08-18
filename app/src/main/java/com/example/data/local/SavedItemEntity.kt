package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_items")
data class SavedItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "TREND", "HOOK", "CREATOR", "KEYWORD", "SCRIPT"
    val title: String,
    val subtitle: String,
    val detailsJson: String,
    val category: String = "General",
    val status: String = "Saved", // "Saved", "In Production", "Scripted", "Published"
    val timestamp: Long = System.currentTimeMillis()
)
