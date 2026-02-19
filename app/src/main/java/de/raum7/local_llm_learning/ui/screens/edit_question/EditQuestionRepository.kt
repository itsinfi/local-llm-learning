package de.raum7.local_llm_learning.ui.screens.edit_question

import de.raum7.local_llm_learning.data.base.BaseRepository
import de.raum7.local_llm_learning.data.database.dao.AnswerDao
import de.raum7.local_llm_learning.data.database.dao.LearningMaterialDao
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.data.database.dao.QuestionDao
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.LearningMaterial

class EditQuestionRepository(
    private val learningMaterialDao: LearningMaterialDao,
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao,
) : BaseRepository() {

    suspend fun getQuestion(questionId: Int): Question = questionDao.getQuestionById(questionId)

    suspend fun getAnswersForQuestion(questionId: Int): List<Answer> = answerDao.getAnswersForQuestion(questionId)

    suspend fun upsertAnswers(answers: List<Answer>) = answerDao.upsertAnswers(answers)

    suspend fun updateQuestion(question: Question) = questionDao.updateQuestion(question)

    suspend fun getLearningMaterialById(id: Int) = learningMaterialDao.getMaterialById(id)

    suspend fun calculateMaterialProgress(learningMaterialId: Int) = questionDao.calculateMaterialProgress(learningMaterialId)

    suspend fun updateLearningMaterial(learningMaterial: LearningMaterial) = learningMaterialDao.updateLearningMaterial(learningMaterial)
}