/**
 * Реализация алгоритма RLE (Run-Length Encoding) - алгоритм сжатия, заменяющий идущие подряд одинаковые символы парой
 * (повторяющийся символ, количество повторений).
 *
 * Автор: Бушуев Никита Федорович
 * Дата: 09.10.2020
 */

fun main() {
    println("test = ${test()}")
}

fun test(): Boolean {
    println("RLE(\"aabbbccda\") = ${runLengthEncoding("aabbbccda")}")
    println("RLE(\"abcddcba\") = ${runLengthEncoding("abcddcba")}")
    println("RLE(\"abaacaaa\") = ${runLengthEncoding("abaacaaa")}")
    println("RLE(\"aaaaaaaa\") = ${runLengthEncoding("aaaaaaaa")}")
    println("RLE(\"\") = ${runLengthEncoding("")}")
    return runLengthEncoding("aabbbccda") == "(a, 2) (b, 3) (c, 2) (d, 1) (a, 1)"
            && runLengthEncoding("abcddcba") == "(a, 1) (b, 1) (c, 1) (d, 2) (c, 1) (b, 1) (a, 1)"
            && runLengthEncoding("abaacaaa") == "(a, 1) (b, 1) (a, 2) (c, 1) (a, 3)"
            && runLengthEncoding("aaaaaaaa") == "(a, 8)"
            && runLengthEncoding("") == ""

}

fun runLengthEncoding(text: String): String {
    var last: Char = text.firstOrNull() ?: return ""
    var counter = 1
    var result = ""
    for (i in 1 until text.length) {
        if (last != text[i]) {
            result += result.withSpacing("($last, $counter)")
            counter = 0
            last = text[i]
        }
        counter++
    }
    if (counter != 0) result += result.withSpacing("($last, $counter)")
    return result
}

fun String.withSpacing(value: String): String {
    return if (this.isEmpty()) value else " $value"
}