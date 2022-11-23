import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import java.io.File

fun main() {
    readFile(fileManager = CyrillicAnswerFileManager("opsta_info_3.txt"))
}

fun readFile(fileManager: QuestionFileManager) {
    val gson = Gson().newBuilder().setPrettyPrinting().create()
    val content = readFileContent(fileManager.filePath)
    var currentIndexInFile = 0
    var currentLineContent = ""
    var question = Question()
    val list = mutableListOf<Question>()
    try {
        content.forEachIndexed { index, line ->
            currentIndexInFile = index + 1
            currentLineContent = line

            val isLineStartsWithAnswerChar =
                line.startsWithAny("a", "b", "v", "g", "d") || line.startsWithAny("a", "b", "c", "d", "e")
            when {
                line.isEmpty() -> {
                    // do nothing
                }
                fileManager.isQuestion(line) -> {
                    question.text = fileManager.processQuestionLine(line)
                }
                isLineStartsWithAnswerChar && line.count() > 1 -> {
                    question.answers.add(Answer(text = fileManager.processAnswerLine(line)))
                }
                isLineStartsWithAnswerChar && line.count() == 1 -> {
                    setCorrectAnswer(line.first(), fileManager.answerMapping, question)
                    list.add(question)
                    question = Question()
                }
            }
        }
        list.forEach {
            val json = gson.toJson(it)
            println(json)
//            request(json)
        }
    } catch (e: Exception) {
        println("Greska na liniji broj $currentIndexInFile.\nSadrzaj linije: $currentLineContent")
        System.err.println(e)
    }
}

fun request(json: String) {
    runBlocking(Dispatchers.IO) {
        HttpClient().post("http://192.168.1.72:3000/questions/create") {
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
        if (this.startsWith(it)) return true
    }
    return false
}

fun readFileContent(path: String): List<String> {
    return File(path).useLines { it.toList() }
}