import java.util.*
import kotlin.collections.LinkedHashMap

fun main() {
    runTests(30)
}

object Huffman {
    fun encode(input: String): Result {
        val queue = PriorityQueue(listOfCharFrequencies(input))
        val map = linkedMapOf<String, String>()
        while (queue.size != 1) {
            val first = queue.poll()
            val second = queue.poll()
            map.updateCode(first.letter, 0)
            map.updateCode(second.letter, 1)

            val word = first.letter + second.letter
            val freq = first.frequency + second.frequency
            queue.add(word to freq)
        }
        val encoded = input.map { map[it.toString()]!! }.toText()

        return Result(encoded, map)
    }

    fun decode(result: Result): String {
        val codes = result.codes.map { it.value to it.key }
        var part = ""
        var decoded = ""
        result.encoded.forEach { digit ->
            part += digit
            codes
                .find { (code, _) -> code == part }
                ?.let { (_, letter) ->
                    part = ""
                    decoded += letter
                }
        }
        return decoded
    }

    /**
     * Возвращает список пар из букв и их частот
     */
    private fun listOfCharFrequencies(input: String) = input.map { it }
        .toSet()
        .map { letter ->
            val probability = input.filter { letter == it }.length

            letter.toString() to probability
        }

    /**
     * Рекурсивно обновляет код по заданному ключу (списку букв)
     */
    private fun MutableMap<String, String>.updateCode(
        key: String,
        number: Int
    ) {
        val head = key.first().toString()
        if (key.length - 1 > 0) {
            set(head, number.toString() + get(head).orEmpty())
            updateCode(key.drop(1), number)
        } else {
            set(head, number.toString() + get(head).orEmpty())
        }
    }

    /**
     * Класс-обертка для возвращения результата кодирования
     * @param encoded закодированная строка
     * @param codes Ассоциативный массив букв и их кодов
     */
    data class Result(val encoded: String, val codes: LinkedHashMap<String, String>)
}