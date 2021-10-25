/**
 * Лабораторная работа №6
 * LZ77 - encoder, decoder
 *
 * Количество решенных задач: 1
 * Общее количество задач: 2
 *
 * Автор: Бушуев Никита Федорович
 * Дата: 28.11.2020, 13.12.2020
 * Kotlin: 1.4.21 (jvm)
 */

fun main() {
    runRandomTests(500)
}

object LZ77 {
    private const val END_MARKER = '\u0000'

    fun encode(text: String, bufferSize: Int = text.length + 1): Result {
        val input = text + END_MARKER
        var buffer = input.takeOrLess(bufferSize)
        val codes = mutableListOf<Code>()
        var dict = ""
        var i = 0
        while (buffer.isNotEmpty()) {
            val match = buffer.takeWhile { substring -> substring in dict }
            val length = match.length
            codes += when (length) {
                1 -> {
                    val offset = dict.getOffsetOrZero(match, i)
                    Code(
                        offset = offset,
                        length = if (offset != 0) 1 else 0,
                        lastLetter = if (offset != 0) END_MARKER else match.last()
                    )
                }
                bufferSize -> {
                    Code(
                        offset = dict.getOffsetOrZero(match, i),
                        length = length,
                        lastLetter = input.getOrElse(i + 1) { END_MARKER }
                    )
                }
                else -> {
                    val matchWithoutLast = match.dropLast(1)
                    Code(
                        offset = dict.getOffsetOrZero(matchWithoutLast, i),
                        length = matchWithoutLast.length,
                        lastLetter = match.last()
                    )
                }
            }
            buffer = buffer.drop(length) + input.substringOrLess(i, i + length)
            i += length
            dict += match
        }
        return Result(codes)
    }

    fun decode(result: Result): String {
        val out = result.codes.fold("") { acc, code ->
            val startIndex = acc.length - code.offset
            val substring = acc.substring(startIndex, startIndex + code.length)
            acc + substring + code.lastLetter
        }
        return if (out.lastOrNull() == END_MARKER) out.dropLast(1) else out
    }

    private inline fun String.takeWhile(predicate: (String) -> Boolean): String {
        for (i in indices) {
            val part = take(i + 1)
            if (!predicate(part)) return part
        }
        return this
    }

    private fun String.getOffsetOrZero(match: String, currentPos: Int) =
        this.lastIndexOf(match).let { startPos ->
            if (startPos == -1) 0 else currentPos - startPos
        }

    private fun String.takeOrLess(takeSize: Int) =
        if (length < takeSize) this else take(takeSize)

    // TODO: should be fixed, not working correctly...
    private fun String.substringOrLess(start: Int, end: Int) =
        when {
            start < 0 || end < 0 || start < end -> ""
            length in start + 1 until end + 1 -> substring(start, length - 1)
            start <= length && end <= length -> substring(start, end)
            else -> ""
        }

    data class Result(val codes: List<Code>) {
        override fun toString(): String {
            return codes.joinToString(separator = " ", prefix = "", postfix = "")
        }
    }

    data class Code(
        val offset: Int = 0,
        val length: Int = 0,
        val lastLetter: Char
    ) {
        override fun toString(): String {
            return "[$offset,$length,'$lastLetter\']"
        }
    }
}