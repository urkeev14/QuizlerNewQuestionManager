class ChatGptQuestionManager constructor(filePath: String, private val mode: Mode) : QuestionManager(
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

    override fun getModeId(): String {
        return mode.id
    }
}

enum class Mode(val id: String) {
    Sport("61ab38f844cfe8c2dc71372d"),
    Music("61ab38f844cfe8c2dc71372e"),
    History("61ab38f844cfe8c2dc71372f"),
    Geography("61ab38f844cfe8c2dc713730"),
    Movie("61ab38f844cfe8c2dc713731"),
    General("61ab38f844cfe8c2dc713732"),
    Literature("61ab38f844cfe8c2dc713745")
}