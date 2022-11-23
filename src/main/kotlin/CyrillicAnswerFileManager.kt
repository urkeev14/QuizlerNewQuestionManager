class CyrillicAnswerFileManager(filePath: String) : QuestionFileManager(
    filePath = filePath,
    isShouldTrimNumbersFromQuestion = true,
    answerMapping = mapOf(
        'a' to 0,
        'b' to 1,
        'v' to 2,
        'g' to 3,
        'd' to 4
    )
) {
    override fun isQuestion(line: String): Boolean {
        return line.first().isDigit()
    }

    override fun processQuestionLine(line: String): String {
        return line.split(".")[1].replace("„", "").replace("“", "").trim().capitalize()
    }

    override fun processAnswerLine(line: String): String {
        return line.drop(2).dropLast(1).trim()
    }
}