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
    val encodedList = encodeWithShannonMethod(input())
    if (encodedList.isPrefixed) {
        println(encodedList)
    } else {
        println("Построенный код не является префиксным!")
    }
}

/** Считывает список целочисленных значений из консоли */
fun input(): List<Int> =
    readLine()!!
        .split(' ')
        .map { it.toInt() }

/** Производит кодирование методом Шеннона */
fun encodeWithShannonMethod(lengths: List<Int>): List<List<Int>> {
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

    return lengths.fold(mutableListOf<MutableList<Int>>()) { acc, length ->
        val twoInNegativePower =
            filledList(length - 1).apply { add(1) }
        val previous = acc.lastOrNull()
            ?: filledList(length).also { acc.add(it) }

        acc.apply { add(previous addTo twoInNegativePower) }
    }.dropLast(1)
}

/**
 * Свойство isPrefixed, вычисляющее является ли построенный
 * код префиксным
 */
val List<List<Int>>.isPrefixed: Boolean
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
infix fun MutableList<Int>.addTo(other: MutableList<Int>): MutableList<Int> {
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
fun Int.toBinaryList() = toString(radix = 2).map {
    it.toString().toInt()
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
    first: MutableList<Int>,
    second: MutableList<Int>,
    value: Int = 0
): Pair<MutableList<Int>, MutableList<Int>> {
    if (first.size < second.size) {
        first.addAll(filledList(second.size - first.size, value))
    } else {
        second.addAll(filledList(first.size - second.size, value))
    }
    return first to second
}