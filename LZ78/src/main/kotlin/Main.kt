/**
 * Лабораторная работа №6
 * LZ78 - encoder, decoder
 *
 * Количество решенных задач: 1
 * Общее количество задач: 2
 *
 * Автор: Бушуев Никита Федорович
 * Дата: 13.12.2020, 19.12.2020
 * Kotlin: 1.4.21 (jvm)
 */

fun main() {
    runRandomTests(1000)
}

object LZ78 {
    private const val END_MARKER = '\u0000'

    fun encode(text: String): Result {
        val input = text + END_MARKER
        val buffer = mutableMapOf<String, Int>()
        val codes = mutableListOf<Code>()
        var i = 0
        while (i < input.length) {
            var match = ""
            var part = input[i].toString()
            while (part !in buffer && input[i].isNotEnd()) {
                codes += Code(letter = part)
                buffer[part] = buffer.size + 1
                part = input[++i].toString()
            }
            if (input[i].isEnd()) break
            do match += input[i]
            while (match in buffer && input[i++].isNotEnd())

            buffer.run {
                get(match.dropLast(1))?.let { pos ->
                    codes += Code(pos, input[i].toString())
                }
                set(match, size + 1)
            }
            i++
        }
        return Result(codes)
    }

    fun decode(encoded: Result): String {
        val codes = encoded.codes
        return codes.fold(DecodeData()) { acc, code ->
            var match = ""
            if (code.number != 0) {
                match += acc.map.getOrDefault(code.number, "")
                acc.result += match
            }
            acc.apply {
                result += code.letter
                map[map.size + 1] = match + code.letter
            }
        }.result.run {
            if (lastOrNull().isEnd()) dropLast(1) else this
        }
    }

    data class Result(val codes: List<Code>) {
        override fun toString(): String {
            return codes.joinToString(separator = " ", prefix = "", postfix = "")
        }
    }

    private fun Char?.isEnd() = this == END_MARKER

    private fun Char?.isNotEnd() = !isEnd()

    data class DecodeData(val map: MutableMap<Int, String> = mutableMapOf(), var result: String = "")

    data class Code(val number: Int = 0, val letter: String) {
        override fun toString(): String {
            return "[$number,'$letter\']"
        }
    }
}
