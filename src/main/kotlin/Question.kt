data class Question(
    var text: String = "",
    val isApproved: Boolean = false,
    val countAnsweredCorrect: Int = 0,
    val countAnsweredWrong: Int = 0,
    val categoryId: String = "61ab38f844cfe8c2dc713732",
    val answers: MutableList<Answer> = mutableListOf(),
)

data class Answer(
    val text: String,
    var isCorrect: Boolean = false,
)