package de.raum7.local_llm_learning.ui.screens.edit_question

import de.raum7.local_llm_learning.data.base.BaseRepository
import de.raum7.local_llm_learning.data.database.dao.AnswerDao
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.data.database.dao.QuestionDao
import de.raum7.local_llm_learning.data.models.Answer

class EditQuestionRepository(
//    private val data: List<LearningMaterial>,
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao,
) : BaseRepository() {
//    fun getQuestion(questionId: String, learningMaterialId: String): Question {
//        val learningMaterial: LearningMaterial = data.firstOrNull { it.id == learningMaterialId } ?:error("LearningMaterial with id $learningMaterialId not found")
//        return learningMaterial.questions.firstOrNull { it.id == questionId } ?:error("Question with id $questionId not found in LearningMaterial with id $learningMaterialId ")
//    }

    suspend fun getQuestion(questionId: Int): Question = questionDao.getQuestionById(questionId)

    suspend fun getAnswersForQuestion(questionId: Int): List<Answer> = answerDao.getAnswersForQuestion(questionId)
}