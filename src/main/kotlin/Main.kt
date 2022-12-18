import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import java.io.File

fun main() {
    readQuestions(fileManager = ChatGptQuestionManager("opsta_info_muzika.txt", Mode.Music))
}

fun readQuestions(fileManager: QuestionManager) {
    val gson = Gson().newBuilder().setPrettyPrinting().create()
    val content = readFileContent(fileManager.filePath)
    val list = mutableListOf<Question>()
    var currentIndexInFile = 0
    var currentLineContent = ""
    var question = createNewQuestion(fileManager)
    try {
        content.forEachIndexed { index, line ->

            currentIndexInFile = index + 1
            currentLineContent = line

            when {
                line.isEmpty() -> {
                    // do nothing
                }
                fileManager.isQuestion(line) -> {
                    question.text = fileManager.processQuestionLine(line)
                }
                fileManager.isAnswerLine(line) -> {
                    question.answers.add(Answer(text = fileManager.processAnswerLine(line)))
                }
                fileManager.isCorrectAnswerChar(line) -> {
                    setCorrectAnswer(line.first(), fileManager.answerMapping, question)
                    list.add(question.copy(answers = question.answers.shuffled().toMutableList()))
                    question = createNewQuestion(fileManager)
                }
            }
        }
        list.forEach {
            val json = gson.toJson(it)
            println(json)
            // TODO: Uncomment line below to send questions to server. REMEMBER TO CHANGE IP ADDRESS IF NEEDED !
              request(json)
        }
        println("================== Broj pitanja: " + list.count())
    } catch (e: Exception) {
        println("Greska na liniji broj $currentIndexInFile.\nSadrzaj linije: $currentLineContent")
        System.err.println(e)
    }
}

private fun createNewQuestion(fileManager: QuestionManager) = Question(
    isApproved = true,
    categoryId = fileManager.getModeId()
)

fun request(json: String) {
    runBlocking(Dispatchers.IO) {
        HttpClient().post("http://192.168.1.72:2000/questions/create") {
            contentType(io.ktor.http.ContentType.Application.Json)
            setBody(json)
        }
    }
}

fun setCorrectAnswer(c: Char, answerMapping: Map<Char, Int>, question: Question): Question {
    val index = answerMapping[c]
    index?.let {
        question.answers[index].isCorrect = true
    }
    return question
}

fun String.startsWithAny(vararg values: String): Boolean {
    values.forEach {
        if (this.startsWith(it, true)) return true
    }
    return false
}

fun readFileContent(path: String): List<String> {
    return File(path).useLines { it.toList() }
}