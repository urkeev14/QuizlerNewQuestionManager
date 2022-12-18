class LatinAnswerFileManager(filePath: String) : QuestionFileManager(
    filePath = filePath,
    isShouldTrimNumbersFromQuestion = false,
    answerMapping = mapOf(
        'A' to 0,
        'B' to 1,
        'C' to 2,
        'D' to 3,
        'E' to 4
    )
) {
    override fun isQuestion(line: String): Boolean {
        return line.endsWith("?") || line.endsWith(":")
    }

    override fun isAnswerLine(line: String): Boolean {
        return line.startsWithAny("A)", "B)", "C)", "D)")
    }

    override fun isCorrectAnswerChar(line: String): Boolean {
        return line.count() == 1 && line.startsWithAny("A", "B", "C", "D")
    }

    override fun processQuestionLine(line: String): String {
        return line
    }

    override fun processAnswerLine(line: String): String {
        return line.drop(2).trim().capitalize()
    }
}