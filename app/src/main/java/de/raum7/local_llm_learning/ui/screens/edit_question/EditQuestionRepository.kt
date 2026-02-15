package de.raum7.local_llm_learning.ui.screens.edit_question

import de.raum7.local_llm_learning.data.base.BaseRepository
import de.raum7.local_llm_learning.data.database.dao.AnswerDao
import de.raum7.local_llm_learning.data.models.Question
import de.raum7.local_llm_learning.data.database.dao.QuestionDao
import de.raum7.local_llm_learning.data.models.Answer

class EditQuestionRepository(
    private val questionDao: QuestionDao,
    private val answerDao: AnswerDao,
) : BaseRepository() {

    suspend fun getQuestion(questionId: Int): Question = questionDao.getQuestionById(questionId)

    suspend fun getAnswersForQuestion(questionId: Int): List<Answer> = answerDao.getAnswersForQuestion(questionId)

    suspend fun upsertAnswers(answers: List<Answer>) = answerDao.upsertAnswers(answers)
}