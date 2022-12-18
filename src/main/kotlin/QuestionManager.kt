abstract class QuestionManager(
    val filePath: String,
    val answerMapping: Map<Char, Int>,
    val isShouldTrimNumbersFromQuestion: Boolean
) {
    abstract fun isQuestion(line: String): Boolean
    abstract fun isAnswerLine(line: String): Boolean
    abstract fun isCorrectAnswerChar(line: String): Boolean
    abstract fun processQuestionLine(line: String): String
    abstract fun processAnswerLine(line: String): String

    abstract fun getModeId(): String
}