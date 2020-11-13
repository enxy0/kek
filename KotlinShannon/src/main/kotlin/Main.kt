import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log2
import kotlin.math.pow

/**
 * Метод Шеннона, код Шеннона-Фано, код Гилберта-Мура
 *
 * Количество решенных задач: 3
 * Общее количество задач: 3
 *
 * Автор: Бушуев Никита Федорович
 * Дата: 05.11.2020, 13.11.2020
 * Kotlin: 1.4.10 (jvm)
 */


fun main() {
    runTests()
}

fun runTests() {
    // Метод Шеннона
    testHeader("Shannon Method")
    test(
        input = mutableListOf(1..5),
        answer = binaryListOf(
            "0", "10", "110", "1110", "11110"
        ),
        algorithm = ::encodeWithShannonMethod
    )
    test(
        input = mutableListOf(1, 2, 1),
        answer = emptyList(),
        algorithm = ::encodeWithShannonMethod
    )
    test(
        input = mutableListOf(3, 3, 3, 4, 4, 4, 4),
        answer = binaryListOf(
            "000", "001", "010", "0110", "0111", "1000", "1001"
        ),
        algorithm = ::encodeWithShannonMethod
    )

    // Кодирование Шеннона-Фано
    testHeader("Shannon-Fano")
    test(
        input = "aaaaaaabccccddddddd",
        answer = binaryListOf(
            "00", "01", "101", "11110"
        ),
        algorithm = ::encodeWithShannonFano
    )
    test(
        input = "aabbccddeeffgg",
        answer = binaryListOf(
            "000", "001", "010", "011", "100", "101", "110"
        ),
        algorithm = ::encodeWithShannonFano
    )
    test(
        input = "",
        answer = emptyList(),
        algorithm = ::encodeWithShannonFano
    )
    test(
        input = "123456789",
        answer = binaryListOf(
            "0000", "0001", "0011", "0101",
            "0111", "1000", "1010", "1100", "1110"
        ),
        algorithm = ::encodeWithShannonFano
    )

    // Кодирование Гилберт-Мура
    testHeader("Gilbert-Moore")
    test(
        input = "3334444",
        answer = binaryListOf("001", "10"),
        algorithm = ::encodeWithGilbertMoore
    )
    test(
        input = "ab",
        answer = binaryListOf("01", "11"),
        algorithm = ::encodeWithGilbertMoore
    )
    test(
        input = "abcdefghjklmn",
        answer = binaryListOf(
            "00001", "00011", "00110", "01000",
            "01011", "01101", "10000", "10010",
            "10100", "10111", "11001", "11100",
            "11110"
        ),
        algorithm = ::encodeWithGilbertMoore
    )
}

fun <T> test(
    input: T,
    answer: List<BinaryList>,
    algorithm: (T) -> List<BinaryList>
) {
    val codes = algorithm(input)
    val isPrefixed = codes.isPrefixed
    val isCorrect = answer == codes
    println("Input    = ${if (input is String) "\"$input\"" else input.toString()}")
    println("Encoded  = $codes")
    println("Prefixed = ${if (isPrefixed) "yes" else "no"}")
    println("Result   = ${if (isCorrect) "passed" else "error"}")
    println("--------------------------------------------------------------")
}

fun testHeader(header: String) {
    println()
    println()
    println("---------------------- Running tests on ----------------------")
    println("Algorithm: $header")
    println("--------------------------------------------------------------")
}

/** Кодирование методом Шеннона */
fun encodeWithShannonMethod(lengths: BinaryList): List<BinaryList> {
    val kraftCondition = lengths.foldIndexed(0.0) { index, acc, item ->
        val isNotFirstAndNotLast =
            index != 0 && index != lengths.size
        if (isNotFirstAndNotLast && lengths[index - 1] > item) {
            return emptyList()
        }
        acc + 2.0.pow(-item)
    } <= 1

    if (!kraftCondition) {
        return emptyList()
    }

    return lengths.fold(mutableListOf<BinaryList>()) { acc, length ->
        val twoInNegativePower =
            filledList(length - 1).apply { add(1) }
        val previous = acc.lastOrNull()
            ?: filledList(length).also { acc.add(it) }

        acc.apply { add(previous addTo twoInNegativePower) }
    }.dropLast(1)
}

/** Кодирование Шеннона-Фано */
fun encodeWithShannonFano(input: String): List<BinaryList> {
    val map = listOfCharFrequencies(input)
        .sortedByDescending { it.second }
        .toMap()

    val cumulativeList = map.values
        .runningFold(0.0) { acc, item -> acc + item }
        .dropLast(1)
        .toMutableList()

    val encodedList = mutableListOf<BinaryList>()
    map.values.forEachIndexed { index, freq ->
        val encodedValue = mutableListOf<Int>()
        val digit = ceil(-log2(freq)).toInt()
        cumulativeList[index] *= 2.0

        for (j in 1 until digit) {
            encodedValue += floor(cumulativeList[index]).toInt()
            if (cumulativeList[index] >= 1) {
                cumulativeList[index]--
            }
            cumulativeList[index] *= 2.0
        }

        encodedValue += floor(cumulativeList[index]).toInt()
        encodedList += encodedValue
    }

    return encodedList
}

/** Кодирование Шеннона-Фано */
fun encodeWithGilbertMoore(input: String): List<BinaryList> {
    val map = listOfCharFrequencies(input)
        .toMap()

    val frequencies = map.values.toMutableList()
    val cumulativeList = mutableListOf(0.0)
    val deltaList = mutableListOf(frequencies.first() / 2.0)

    (0 until frequencies.size).forEach { index ->
        if (index != frequencies.size - 1) {
            cumulativeList += cumulativeList[index] + frequencies[index]
            deltaList += cumulativeList[index + 1] + frequencies[index + 1] / 2.0
        }
    }

    val encodedList = mutableListOf<BinaryList>()
    frequencies.forEachIndexed { index, freq ->
        val encodedValue = mutableListOf<Int>()
        val digit = 1 + (ceil(-log2(freq)).toInt())
        deltaList[index] *= 2.0

        for (j in 1 until digit) {
            encodedValue += floor(deltaList[index]).toInt()
            if (deltaList[index] >= 1) {
                deltaList[index]--
            }
            deltaList[index] *= 2.0
        }

        encodedValue += floor(deltaList[index]).toInt()
        encodedList += encodedValue
    }

    return encodedList
}

/**
 * Возвращает список пар из букв и их частот
 */
fun listOfCharFrequencies(input: String) = input.map { it }
    .toSet()
    .map { letter ->
        val count = input.filter { letter == it }.length
        val probability = count / input.length.toDouble()

        letter to probability
    }

/**
 * Свойство isPrefixed, вычисляющее является ли построенный
 * код префиксным
 */
val List<BinaryList>.isPrefixed: Boolean
    get() {
        forEachIndexed { i, item ->
            for (j in i + 1 until size)
                if (item == take(item.size))
                    return false
        }
        return this.isNotEmpty()
    }


/*********************************************************
 * Вспомогательные функции, используемые при вычислениях *
 *********************************************************/

/**
 * Складывает два списка в двоичной системе исчисления.
 *
 * Если списки имеют разную длину, то они выравниваются путем добравления
 * нулей в конец меньшего списка.
 */
infix fun BinaryList.addTo(other: BinaryList): BinaryList {
    var remainder = 0
    val (first, second) = syncLists(this, other)
    return first.foldRightIndexed(mutableListOf()) { index, item1, acc ->
        val item2 = second[index]
        acc.add(0, (item1 + item2 + remainder) % 2)
        remainder = if (item1 + item2 + remainder > 1) 1 else 0
        acc
    }
}

/**
 *  Создает изменяемый список заданного размера size,
 *  заполненный значениями value (0 по умолчаниию)
 */
fun filledList(size: Int, value: Int = 0) =
    MutableList(size) { value }

/**
 * Выравнивает (синхронизирует) размер двух списков,
 * путем добавления значений value в конец меньшего
 * из списков.
 */
fun syncLists(
    first: BinaryList,
    second: BinaryList,
    value: Int = 0
): Pair<BinaryList, BinaryList> {
    if (first.size < second.size) {
        first.addAll(filledList(second.size - first.size, value))
    } else {
        second.addAll(filledList(first.size - second.size, value))
    }
    return first to second
}

fun binaryListOf(vararg codes: String): List<BinaryList> = codes.map { code ->
    code.map { digit -> digit.toString().toInt() }.toMutableList()
}

fun mutableListOf(values: IntRange) = values.toMutableList()

typealias BinaryList = MutableList<Int>
