package de.raum7.local_llm_learning.ui.screens.quiz

import de.raum7.local_llm_learning.data.base.BaseRepository
import de.raum7.local_llm_learning.data.models.LearningMaterial
import de.raum7.local_llm_learning.data.database.dao.AnswerDao
import de.raum7.local_llm_learning.data.database.dao.LearningMaterialDao
import de.raum7.local_llm_learning.data.database.dao.QuestionDao
import de.raum7.local_llm_learning.data.models.Answer
import de.raum7.local_llm_learning.data.models.Question

class QuizRepository(
//    private val data: List<LearningMaterial>,
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao,
    private val learningMaterialDao: LearningMaterialDao,
) : BaseRepository() {

    suspend fun getLearningMaterialById(id: Int): LearningMaterial = learningMaterialDao.getMaterialById(id)

    suspend fun getQuestionsForLearningMaterial(learningMaterialId: Int): List<Question> = questionDao.getQuestionsForLearningMaterial(learningMaterialId)

    suspend fun getProgress(id: Int): Double = learningMaterialDao.getProgress(id)

    suspend fun getAnswersForQuestion(id: Int): List<Answer> = answerDao.getAnswersForQuestion(id)

    suspend fun getQuestionCount(learningMaterialId: Int) = questionDao.getQuestionCountForLearningMaterial(learningMaterialId)

    suspend fun getCurrentProgression(learningMaterialId: Int) = questionDao.getCurrentProgression(learningMaterialId)

    suspend fun getMasteredQuestionCount(learningMaterialId: Int) = questionDao.getMasteredQuestionCount(learningMaterialId)

    suspend fun getQuestionCountForLearningMaterial(learningMaterialId: Int): Int = questionDao.getQuestionCountForLearningMaterial(learningMaterialId)

    suspend fun updateLearningMaterial(learningMaterial: LearningMaterial) = learningMaterialDao.updateLearningMaterial(learningMaterial)

    suspend fun updateQuestion(question: Question) = questionDao.updateQuestion(question)

    suspend fun updateQuestions(questions: List<Question>) = questionDao.updateQuestions(questions)

    suspend fun getNextQuestion(learningMaterialId: Int, lookForMissingPriority: Boolean): Question? {
        // if lookForMissingPriority is true, look for a question with priority == null first, else skip that query
        val question: Question? = if (lookForMissingPriority) {
            questionDao.getMissingPriorityQuestion(learningMaterialId) ?: questionDao.getHighestPriorityQuestion(learningMaterialId)
        } else {
            questionDao.getHighestPriorityQuestion(learningMaterialId)
        }
        return question
    }

    suspend fun getAnsweredQuestions(learningMaterialId: Int) = questionDao.getAnsweredQuestions(learningMaterialId)

    suspend fun calculateMaterialProgress(learningMaterialId: Int): Double = questionDao.calculateMaterialProgress(learningMaterialId)
}