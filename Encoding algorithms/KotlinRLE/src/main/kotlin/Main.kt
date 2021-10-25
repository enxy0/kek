/**
 * Реализация алгоритма RLE (Run-Length Encoding)
 *
 * Количество решенных задач: 1
 * Общее количество задач: 1
 *
 * Автор: Бушуев Никита Федорович
 * Дата: 30.10.2020
 * Kotlin: 1.4.10 (jvm)
 */

fun main() {
    runTests()
}

/** Кодирует text с помощью RLE алгоритма */
fun encodeRle(text: String): String {
    var last: Char = text.firstOrNull() ?: return ""
    var counter = 1
    var result = ""
    for (i in 1 until text.length) {
        if (last != text[i]) {
            result += toStringPair(last, counter).plus(" ")
            counter = 0
            last = text[i]
        }
        counter++
    }
    if (counter != 0) result += toStringPair(last, counter)
    return result
}

/** Декодирует text из RLE */
fun decodeRle(text: String): String = if (text.isNotEmpty()) {
    text.split(' ')
        .map { it[1].toString() to it[3].toString().toInt() }
        .fold("") { acc, (letter, counter) ->
            acc + letter.repeat(counter)
        }
} else ""

/** Запускает серию тестов с выводом результата в консоль */
fun runTests() {
    test("")
    test("abc")
    test("abccdd")
    test("aabbbccda")
    test("abcddcba")
    test("abaacaaa")
    test("aaaaaaaa")
}

/** Запускает тест для конкретного значения */
fun test(text: String): Boolean {
    val encodedText = encodeRle(text)
    val decodedText = decodeRle(encodedText)
    val isCorrect = text == decodedText
    println("\"$text\" = \"$encodedText\" : $isCorrect")
    return isCorrect
}

/** Строковое представление пары из буквы и количества ее повторений - (a, 4) */
fun toStringPair(letter: Char, counter: Int) = "($letter,$counter)"