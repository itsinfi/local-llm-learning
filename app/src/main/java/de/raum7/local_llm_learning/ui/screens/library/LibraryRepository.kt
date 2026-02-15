package de.raum7.local_llm_learning.ui.screens.library

import de.raum7.local_llm_learning.data.base.BaseRepository
import de.raum7.local_llm_learning.data.database.dao.LearningMaterialDao
import de.raum7.local_llm_learning.data.database.dao.QuestionDao
import de.raum7.local_llm_learning.data.models.LearningMaterial

class LibraryRepository(
//    private val data: List<LearningMaterial>,
    private val learningMaterialDao: LearningMaterialDao,
    private val questionDao: QuestionDao,
) : BaseRepository() {
    suspend fun getLearningMaterials(): List<LearningMaterial> = learningMaterialDao.getAllMaterials()

    suspend fun getQuestionCountForLearningMaterial(learningMaterialId: Int): Int = questionDao.getQuestionCountForLearningMaterial(learningMaterialId)
}