/**
 * МОНОТОННЫЕ КОДЫ:
 *   - унарный код;
 *   - код Левенштейна, гамма-код Левенштейна;
 *   - коды Элайеса: омега-код, гамма-код, дельта-код;
 *   - коды Ивэн-Родэ;
 *   - код Голомба, код Райса;
 *   - экспоненциальный код Голомба;
 *   - код Фибоначчи;
 *   - шаговые коды: старт-шаг-стоп коды, старт-шаг коды.
 *
 * Автор: Бушуев Никита Федорович
 * Дата: 12.10.2020
 * Kotlin: 1.4.10 (jvm)
 */

import kotlin.math.floor
import kotlin.math.log2

fun main() {
    runTests()
}

/*
 *****************************
 * Раздел функций кодирования
 *****************************
 */

/**
 * 1. Переводит положительное натуральное число в унарный код
 */
fun encodeToUnary(n: Int): List<Int> {
    if (n < 0) throw WrongParameterException("Передан неверный параметр. Число n не может быть меньше 0: n = $n")
    return if (n == 0) {
        listOf(0)
    } else {
        listOf(1) + encodeToUnary(n - 1)
    }
}

/**
 * 2. Переводит число в код Левенштейна
 */
fun encodeLevenshtein(n: Int): List<Int> {
    if (n == 0) return listOf(0)
    var num = n
    var binary = num.toBinaryListDropped()
    val result = arrayListOf<Int>()
    var counter = 1
    while (binary.isNotEmpty()) {
        result.addAll(0, binary)
        num = log2(num)
        binary = num.toBinaryListDropped()
        counter++
    }
    result.add(0, 0)
    result.addAll(0, times(n = counter, value = 1))
//    println("$n, result = $result")
    return result
}

/**
 * 3. Переводит число в Гамма-код Левенштейна
 */
fun encodeGammaLevenshtein(n: Int): List<Int> {
    return when (n) {
        0 -> listOf(0)
        1 -> listOf(1)
        else -> {
            val result = arrayListOf<Int>()
            var num = n
            while (num != 0) {
                result.add(0, 0)
                result.add(0, num % 2)
                num /= 2
            }
            result.removeAt(1)
//            println("$n, result = ${result.reversed()}")
            return result.reversed()
        }
    }
}

/**
 * 4. Переводит число в Омега-код Элайеса
 */
fun encodeEliasOmega(n: Int): List<Int> {
    val result = arrayListOf(0)
    if (n == 1) return result
    var num = n
    var binary = num.toBinaryList()
    while (num != 1) {
        result.addAll(0, binary)
        num = binary.size - 1
        binary = num.toBinaryList()
    }
//    println("$n, result = $result")
    return result
}

/**
 * 5. Переводит число в код Ивэн-Родэ
 */
fun encodeEvenRodeh(n: Int): List<Int> {
    val result = arrayListOf<Int>()
    if (n >= 4) {
        result.add(0)
    }
    if (n < 8) {
        result.addAll(0, n.toBinaryListFixed(size = 3))
//        println("n = $n, result = $result")
        return result
    }
    var num = n
    var binary = num.toBinaryList()
    while (num != 3) {
        result.addAll(0, binary)
        num = binary.size
        binary = num.toBinaryList()
    }
//    println("n = $n, result = $result")
    return result
}

/**
 * 6. Переводит число в Гамма-код Элайеса
 */
fun encodeEliasGamma(n: Int): List<Int> {
    if (n == 0) throw WrongParameterException("Для n = 0 не существует Гамма-кода Элайеса!")
    return encodeToUnary(log2(n)).invertBits() + n.toBinaryListDropped()
}

/**
 * 7. Переводит число в Дельта-код Элайеса
 */
fun encodeEliasDelta(n: Int): List<Int> {
    if (n == 0) throw WrongParameterException("Для n = 0 не существует Дельта-кода Элайеса!")
    return encodeEliasGamma(log2(n) + 1) + n.toBinaryListDropped()
}



/*
 ***********************************************************************
 * Раздел вспомогательный функций (использованных при вычислениях)
 ***********************************************************************
 */

/**
 * Возвращает двоичное представление числа Int в виде List<Int> без первого бита
 */
fun Int.toBinaryListDropped(): List<Int> {
    return toBinaryList().drop(1)
}

/**
 * Возвращает двоичное представление числа Int в виде List<Int>
 */
fun Int.toBinaryList(): List<Int> {
    return this.toString(radix = 2).map { it.toString().toInt() }
}

fun List<Int>.invertBits(): List<Int> {
    return map { if (it == 0) 1 else 0 }
}

/**
 * Возвращает двоичное представление числа Int в виде List<Int> c фиксированной длинной
 * Оставшиеся биты заполняются переданным значением [fill]
 */
fun Int.toBinaryListFixed(size: Int, fill: Int = 0): List<Int> {
    val result = arrayListOf<Int>()
    result.addAll(toBinaryList())
    while (result.size < size) {
        result.add(0, fill)
    }
    return result
}

/**
 * Возвращает список List<Int>, заполненный n количеством раз цифрой value
 */
fun times(n: Int, value: Int): List<Int> {
    val result = mutableListOf<Int>()
    for (i in 0 until n) {
        result.add(value)
    }
    return result
}

/**
 * Возвращает Int результат округления вниз результата логарифма по основанию 2 от x
 */
fun log2(x: Int): Int {
    return floor(log2(x.toFloat())).toInt()
}

/**
 * Exception неверных параметров (данных)
 */
class WrongParameterException(message: String) : Exception(message)


/*
 *************************************************
 * Неудачные тестовые примеры функций кодирования
 *************************************************
 */

/**
 * Запускает тесты по для всех функций кодирования
 */
fun runTests() {
    println("Start testing...")
    println("1. Unary tests: ${unaryTests()}")
    println("2. Levenshtein tests: ${levenshteinTests()}")
    println("3. Levenshtein Gamma tests: ${levenshteinGammaTests()}")
    println("4. Elias Omega tests: ${eliasOmegaTests()}")
    println("5. Even-Rodeh tests: ${evenRodehTests()}")
    println("6. Elias Gamma tests: ${eliasGammaTests()}")
    println("7. Elias Delta tests: ${eliasDeltaTests()}")
}

fun unaryTests(): Boolean {
    return encodeToUnary(1) == listOf(1, 0) &&
        encodeToUnary(2) == listOf(1, 1, 0) &&
        encodeToUnary(3) == listOf(1, 1, 1, 0) &&
        encodeToUnary(4) == listOf(1, 1, 1, 1, 0) &&
        encodeToUnary(5) == listOf(1, 1, 1, 1, 1, 0) &&
        encodeToUnary(6) == listOf(1, 1, 1, 1, 1, 1, 0) &&
        encodeToUnary(7) == listOf(1, 1, 1, 1, 1, 1, 1, 0) &&
        encodeToUnary(8) == listOf(1, 1, 1, 1, 1, 1, 1, 1, 0) &&
        encodeToUnary(9) == listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 0)
}

fun levenshteinTests(): Boolean {
    return encodeLevenshtein(0) == listOf(0) &&
        encodeLevenshtein(1) == listOf(1, 0) &&
        encodeLevenshtein(2) == listOf(1, 1, 0, 0) &&
        encodeLevenshtein(3) == listOf(1, 1, 0, 1) &&
        encodeLevenshtein(4) == listOf(1, 1, 1, 0, 0, 0, 0) &&
        encodeLevenshtein(5) == listOf(1, 1, 1, 0, 0, 0, 1) &&
        encodeLevenshtein(6) == listOf(1, 1, 1, 0, 0, 1, 0) &&
        encodeLevenshtein(7) == listOf(1, 1, 1, 0, 0, 1, 1) &&
        encodeLevenshtein(8) == listOf(1, 1, 1, 0, 1, 0, 0, 0) &&
        encodeLevenshtein(9) == listOf(1, 1, 1, 0, 1, 0, 0, 1) &&
        encodeLevenshtein(10) == listOf(1, 1, 1, 0, 1, 0, 1, 0) &&
        encodeLevenshtein(11) == listOf(1, 1, 1, 0, 1, 0, 1, 1) &&
        encodeLevenshtein(12) == listOf(1, 1, 1, 0, 1, 1, 0, 0) &&
        encodeLevenshtein(13) == listOf(1, 1, 1, 0, 1, 1, 0, 1) &&
        encodeLevenshtein(14) == listOf(1, 1, 1, 0, 1, 1, 1, 0) &&
        encodeLevenshtein(15) == listOf(1, 1, 1, 0, 1, 1, 1, 1) &&
        encodeLevenshtein(16) == listOf(1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0) &&
        encodeLevenshtein(17) == listOf(1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1) &&
        encodeLevenshtein(18) == listOf(1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0) &&
        encodeLevenshtein(19) == listOf(1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1) &&
        encodeLevenshtein(20) == listOf(1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0) &&
        encodeLevenshtein(21) == listOf(1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 1) &&
        encodeLevenshtein(22) == listOf(1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0) &&
        encodeLevenshtein(23) == listOf(1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1) &&
        encodeLevenshtein(24) == listOf(1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0)
}

fun levenshteinGammaTests(): Boolean {
    return encodeGammaLevenshtein(0) == listOf(0) &&
        encodeGammaLevenshtein(1) == listOf(1) &&
        encodeGammaLevenshtein(5) == listOf(0, 1, 0, 0, 1) &&
        encodeGammaLevenshtein(13) == listOf(0, 1, 0, 0, 0, 1, 1) &&
        encodeGammaLevenshtein(20) == listOf(0, 0, 0, 0, 0, 1, 0, 0, 1) &&
        encodeGammaLevenshtein(63) == listOf(0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1) &&
        encodeGammaLevenshtein(129) == listOf(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1)
}

fun eliasOmegaTests(): Boolean {
    return encodeEliasOmega(1) == listOf(0) &&
        encodeEliasOmega(2) == listOf(1, 0, 0) &&
        encodeEliasOmega(3) == listOf(1, 1, 0) &&
        encodeEliasOmega(4) == listOf(1, 0, 1, 0, 0, 0) &&
        encodeEliasOmega(5) == listOf(1, 0, 1, 0, 1, 0) &&
        encodeEliasOmega(6) == listOf(1, 0, 1, 1, 0, 0) &&
        encodeEliasOmega(7) == listOf(1, 0, 1, 1, 1, 0) &&
        encodeEliasOmega(8) == listOf(1, 1, 1, 0, 0, 0, 0) &&
        encodeEliasOmega(9) == listOf(1, 1, 1, 0, 0, 1, 0) &&
        encodeEliasOmega(10) == listOf(1, 1, 1, 0, 1, 0, 0) &&
        encodeEliasOmega(11) == listOf(1, 1, 1, 0, 1, 1, 0) &&
        encodeEliasOmega(12) == listOf(1, 1, 1, 1, 0, 0, 0) &&
        encodeEliasOmega(13) == listOf(1, 1, 1, 1, 0, 1, 0) &&
        encodeEliasOmega(14) == listOf(1, 1, 1, 1, 1, 0, 0) &&
        encodeEliasOmega(15) == listOf(1, 1, 1, 1, 1, 1, 0) &&
        encodeEliasOmega(16) == listOf(1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0) &&
        encodeEliasOmega(17) == listOf(1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0)
}

fun evenRodehTests(): Boolean {
    return encodeEvenRodeh(0) == listOf(0, 0, 0) &&
        encodeEvenRodeh(1) == listOf(0, 0, 1) &&
        encodeEvenRodeh(2) == listOf(0, 1, 0) &&
        encodeEvenRodeh(3) == listOf(0, 1, 1) &&
        encodeEvenRodeh(4) == listOf(1, 0, 0, 0) &&
        encodeEvenRodeh(5) == listOf(1, 0, 1, 0) &&
        encodeEvenRodeh(6) == listOf(1, 1, 0, 0) &&
        encodeEvenRodeh(7) == listOf(1, 1, 1, 0) &&
        encodeEvenRodeh(8) == listOf(1, 0, 0, 1, 0, 0, 0, 0) &&
        encodeEvenRodeh(9) == listOf(1, 0, 0, 1, 0, 0, 1, 0) &&
        encodeEvenRodeh(15) == listOf(1, 0, 0, 1, 1, 1, 1, 0) &&
        encodeEvenRodeh(16) == listOf(1, 0, 1, 1, 0, 0, 0, 0, 0)
}

fun eliasGammaTests(): Boolean {
    return encodeEliasGamma(1) == listOf(1) &&
        encodeEliasGamma(2) == listOf(0, 1, 0) &&
        encodeEliasGamma(3) == listOf(0, 1, 1) &&
        encodeEliasGamma(4) == listOf(0, 0, 1, 0, 0) &&
        encodeEliasGamma(5) == listOf(0, 0, 1, 0, 1) &&
        encodeEliasGamma(6) == listOf(0, 0, 1, 1, 0) &&
        encodeEliasGamma(7) == listOf(0, 0, 1, 1, 1) &&
        encodeEliasGamma(8) == listOf(0, 0, 0, 1, 0, 0, 0) &&
        encodeEliasGamma(9) == listOf(0, 0, 0, 1, 0, 0, 1) &&
        encodeEliasGamma(10) == listOf(0, 0, 0, 1, 0, 1, 0) &&
        encodeEliasGamma(11) == listOf(0, 0, 0, 1, 0, 1, 1) &&
        encodeEliasGamma(12) == listOf(0, 0, 0, 1, 1, 0, 0) &&
        encodeEliasGamma(13) == listOf(0, 0, 0, 1, 1, 0, 1) &&
        encodeEliasGamma(14) == listOf(0, 0, 0, 1, 1, 1, 0)

}

fun eliasDeltaTests(): Boolean {
    return encodeEliasDelta(1) == listOf(1) &&
        encodeEliasDelta(2) == listOf(0, 1, 0, 0) &&
        encodeEliasDelta(3) == listOf(0, 1, 0, 1) &&
        encodeEliasDelta(4) == listOf(0, 1, 1, 0, 0) &&
        encodeEliasDelta(5) == listOf(0, 1, 1, 0, 1) &&
        encodeEliasDelta(6) == listOf(0, 1, 1, 1, 0) &&
        encodeEliasDelta(7) == listOf(0, 1, 1, 1, 1) &&
        encodeEliasDelta(8) == listOf(0, 0, 1, 0, 0, 0, 0, 0) &&
        encodeEliasDelta(9) == listOf(0, 0, 1, 0, 0, 0, 0, 1) &&
        encodeEliasDelta(10) == listOf(0, 0, 1, 0, 0, 0, 1, 0) &&
        encodeEliasDelta(11) == listOf(0, 0, 1, 0, 0, 0, 1, 1) &&
        encodeEliasDelta(12) == listOf(0, 0, 1, 0, 0, 1, 0, 0) &&
        encodeEliasDelta(13) == listOf(0, 0, 1, 0, 0, 1, 0, 1) &&
        encodeEliasDelta(14) == listOf(0, 0, 1, 0, 0, 1, 1, 0) &&
        encodeEliasDelta(15) == listOf(0, 0, 1, 0, 0, 1, 1, 1) &&
        encodeEliasDelta(16) == listOf(0, 0, 1, 0, 1, 0, 0, 0, 0) &&
        encodeEliasDelta(17) == listOf(0, 0, 1, 0, 1, 0, 0, 0, 1)
}