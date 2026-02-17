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

    suspend fun getNextQuestionById(id: Int, learningMaterialId: Int): Question = questionDao.getNextQuestionById(id, learningMaterialId)

    suspend fun getAnswersForQuestion(id: Int): List<Answer> = answerDao.getAnswersForQuestion(id)

    suspend fun getQuestionCountForLearningMaterial(learningMaterialId: Int): Int = questionDao.getQuestionCountForLearningMaterial(learningMaterialId)

    suspend fun updateLearningMaterial(learningMaterial: LearningMaterial) = learningMaterialDao.updateLearningMaterial(learningMaterial)

    suspend fun updateQuestion(question: Question) = questionDao.updateQuestion(question)

    suspend fun updateQuestions(questions: List<Question>) = questionDao.updateQuestions(questions)

    suspend fun getHighestPriorityQuestion(learningMaterialId: Int): Question = questionDao.getHighestPriorityQuestion(learningMaterialId)

    suspend fun getNextHighestPriorityQuestion(id: Int, learningMaterialId: Int): Question = questionDao.getNextHighestPriorityQuestion(id, learningMaterialId)

    suspend fun getMissingPriorityQuestion(learningMaterialId: Int): Question? = questionDao.getMissingPriorityQuestion(learningMaterialId)

    suspend fun getFirstQuestion(learningMaterialId: Int): Question {
        var question: Question? = questionDao.getMissingPriorityQuestion(learningMaterialId)
        return question ?: questionDao.getHighestPriorityQuestion(learningMaterialId)
    }

    suspend fun getNextQuestion(id: Int, learningMaterialId: Int): Question {
        var question: Question? = questionDao.getNextMissingPriorityQuestion(id, learningMaterialId)
        return question ?: questionDao.getNextHighestPriorityQuestion(id, learningMaterialId)
    }

    suspend fun getAnsweredQuestions(learningMaterialId: Int) = questionDao.getAnsweredQuestions(learningMaterialId)
}