abstract class QuestionFileManager(
    val filePath: String,
    val answerMapping: Map<Char, Int>,
    val isShouldTrimNumbersFromQuestion: Boolean
) {
    abstract fun isQuestion(line: String): Boolean
    abstract fun processQuestionLine(line: String): String
    abstract fun processAnswerLine(line: String): String
}