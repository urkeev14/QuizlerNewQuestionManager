import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import java.io.File


val map = mapOf(
    'a' to 0,
    'b' to 1,
    'v' to 2,
    'g' to 3,
    'd' to 4
)

fun main() {
    val gson = Gson().newBuilder().setPrettyPrinting().create()
    val content = readFileContent("opsta_info_novo.txt")
    var question = Question()
    val list = mutableListOf<Question>()
    content.forEach { line ->
        when {
            line.isEmpty() -> {
                list.add(question)
                question = Question()
            }
            line.first().isDigit() -> {
                val droppedAndTrimmed = line.split(".")[1].trim()
                question.text = droppedAndTrimmed
            }
            line.startsWithAny("a)", "b)", "v)", "g)", "d)") -> {
                question.answers.add(Answer(text = line.drop(2).dropLast(1).trim()))
            }
            line.startsWithAny("a", "b", "v", "g", "d") && line.count() == 1 -> {
                setCorrectAnswer(line.first(), question)
            }
        }
    }
    list.forEach {
        val json = gson.toJson(it)
        println(json)
        request(gson.toJson(it))
    }
}

fun request(json: String) {
    runBlocking {
        HttpClient().post("http://192.168.1.72:2000/questions/create") {
            setBody(json)
        }
    }
}

fun setCorrectAnswer(c: Char, question: Question): Question {
    val index = map[c]
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