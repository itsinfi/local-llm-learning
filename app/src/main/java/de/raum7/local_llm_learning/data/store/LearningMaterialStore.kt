package de.raum7.local_llm_learning.data.store

import android.content.Context
import de.raum7.local_llm_learning.data.models.LearningMaterial
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LearningMaterialStore {

    private val _items = MutableStateFlow<List<LearningMaterial>>(emptyList())
    val items: StateFlow<List<LearningMaterial>> = _items

    private var initialized = false
    private lateinit var appContext: Context

    fun init(context: Context) {
        if (initialized) return
        appContext = context.applicationContext
        _items.value = LearningMaterialDiskStore.load(appContext)
        initialized = true
    }

    fun add(material: LearningMaterial) {
        _items.value = _items.value + material
        persist()
    }

    fun getById(id: String): LearningMaterial? {
        return _items.value.firstOrNull { it.id == id }
    }

    fun replaceAll(materials: List<LearningMaterial>) {
        _items.value = materials
        persist()
    }

    private fun persist() {
        if (!initialized) return
        LearningMaterialDiskStore.save(appContext, _items.value)
    }
}
