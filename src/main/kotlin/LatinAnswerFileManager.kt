class LatinAnswerFileManager(filePath: String) : QuestionFileManager(
    filePath = filePath,
    isShouldTrimNumbersFromQuestion = false,
    answerMapping = mapOf(
        'a' to 0,
        'b' to 1,
        'c' to 2,
        'd' to 3,
        'e' to 4
    )
) {
    override fun isQuestion(line: String): Boolean {
        return line.first().isUpperCase()
    }

    override fun processQuestionLine(line: String): String {
        return line
    }

    override fun processAnswerLine(line: String): String {
        return line.drop(2).trim().capitalize()
    }
}