/**
 * Перевод чисел из десятичной системы исчисления в нега-двоичную и обратно.
 * Числа ограничены накладываемой маской (0xA..A) и размером типа Int
 *
 * Автор: Бушуев Никита.
 * Дата: 29.09.2020
 */

fun main() {
    println(" 15(10) = 10011(-2): ${"15" == 10011.fromNega() && "10011" == 15.toNega()}")
    println("-15(10) = 110001(-2): ${"-15" == 110001.fromNega() && "110001" == (-15).toNega()}")
}

/**
 * Переводит десятичное число типа Int в нега-двоичное число (строковое представление)
 */
fun Int.toNega(): String {
    return ((this + 0xAAAAAAA) xor 0xAAAAAAA).toString(radix = 2)
}

/**
 * Переводит нега-двоичное число типа Int в десятичное число (строковое представление)
 */
fun Int.fromNega(): String {
    // Число переводится из нега-двоичной в десятичную (с помощью алгоритма перевода двоичного числа)
    val decimal = this.toString(radix = 10).toInt(radix = 2)
    return ((decimal xor 0xAAAAAAA) - 0xAAAAAAA).toString(radix = 10)
}