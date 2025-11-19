package com.xtremeclicker.pro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.xtremeclicker.pro.model.Action

@Database(entities = [SavedScript::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ScriptDatabase : RoomDatabase() {
    abstract fun scriptDao(): ScriptDao

    companion object {
        @Volatile
        private var INSTANCE: ScriptDatabase? = null

        fun getDatabase(context: Context): ScriptDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScriptDatabase::class.java,
                    "xtreme_scripts_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromActionList(actions: List<Action>): String {
        return gson.toJson(actions)
    }

    @TypeConverter
    fun toActionList(json: String): List<Action> {
        val type = object : TypeToken<List<Action>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    @TypeConverter
    fun fromLongList(list: List<Long>): String = gson.toJson(list)

    @TypeConverter
    fun toLongList(json: String): List<Long> {
        val type = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
