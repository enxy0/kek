import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.pow

/**
 * Метод Шеннона, код Шеннона-Фано, код Гилберта-Мура
 *
 * Количество решенных задач: 1
 * Общее количество задач: 3
 *
 * Автор: Бушуев Никита Федорович
 * Дата: 05.11.2020
 * Kotlin: 1.4.10 (jvm)
 *
 */


fun main() {
//    Метод Шеннона
//    val encodedList = encodeWithShannonMethod(inputNumbers())
//    if (encodedList.isPrefixed) {
//        println(encodedList)
//    } else {
//        println("Построенный код не является префиксным!")
//    }

// Код Шеннона-Фано
    encodeWithShannonFano(inputString())
}

/** Считывает список целочисленных значений из консоли */
fun inputNumbers(): BinaryList =
    readLine()!!
        .split(' ')
        .map { it.toInt() }
        .toMutableList()

fun inputString(): String = readLine()!!.toString()

/** Производит кодирование методом Шеннона */
fun encodeWithShannonMethod(lengths: BinaryList): List<BinaryList> {
    val kraftCondition = lengths.foldIndexed(0.0) { index, acc, item ->
        val isNotFirstAndNotLast =
            index != 0 && index != lengths.size
        if (isNotFirstAndNotLast && lengths[index - 1] > item) {
            println("Заданные длины не удовлетворяют условию Шеннона.")
            return emptyList()
        }
        acc + 2.0.pow(-item)
    } <= 1

    if (!kraftCondition) {
        println("Заданные длины не удовлетворяют неравенству Крафта.")
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

fun encodeWithShannonFano(input: String): List<BinaryList> {
    val map = input.map { it }
        .toSet()
        .map { letter ->
            val count = input.filter { letter == it }.length
            val probability = count / input.length.toDouble()

            letter to probability
        }
        .sortedByDescending { it.second }
        .toMap()
        .also { println(it) }

    val cumulativeList = map.values
        .runningFold(0.0) { acc, item -> acc + item }
        .dropLast(1)
        .toMutableList()

    val words = map.toList().forEachIndexed { index, (_, freq) ->
        val digit = ceil(log2(freq)).toInt()
        cumulativeList[index] *= 2.0

        for (j in 1 until digit) {
            
        }
    }
    return emptyList()
}

/**
 * Свойство isPrefixed, вычисляющее является ли построенный
 * код префиксным
 */
val List<BinaryList>.isPrefixed: Boolean
    get() {
        for (i in indices) {
            for (j in i + 1 until size) {
                var isNotEqual = false
                for (k in this[i].indices) {
                    if (this[i][k] != this[j][k]) {
                        isNotEqual = true
                    }
                }
                if (!isNotEqual) {
                    return false
                }
            }
        }
        return true
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

/** Возвращает двоичное представление числа Int в виде List<Int> */
fun Int.toBinaryList(): BinaryList = toString(radix = 2).map {
    it.toString().toInt()
}.toMutableList()

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

typealias BinaryList = MutableList<Int>
