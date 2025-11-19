package com.xtremeclicker.pro.ui.scripts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.xtremeclicker.pro.database.ScriptDatabase
import com.xtremeclicker.pro.database.SavedScript
import com.xtremeclicker.pro.model.Action
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.google.gson.Gson

class ScriptsViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = ScriptDatabase.getDatabase(application).scriptDao()
    val allScripts: Flow<List<SavedScript>> = dao.getAllScripts()

    fun deleteScript(script: SavedScript) {
        viewModelScope.launch {
            dao.delete(script)
        }
    }

    fun saveScript(name: String, description: String, actions: List<Action>) {
        viewModelScope.launch {
            val json = Gson().toJson(actions)
            val script = SavedScript(
                name = name,
                description = description,
                actionsJson = json
            )
            dao.insert(script)
        }
    }

    suspend fun getScript(id: Long): SavedScript? = dao.getScriptById(id)
}
