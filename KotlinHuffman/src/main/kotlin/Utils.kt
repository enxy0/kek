/**
 * Своя реализация пары, где letter - буква, а frequency - частота
 */
data class FrequencyPair(val letter: String, val frequency: Int): Comparable<FrequencyPair> {

    override fun toString(): String {
        return "($letter, $frequency)"
    }

    override fun compareTo(other: FrequencyPair): Int = frequency.compareTo(other.frequency)
}

fun<T> List<T>.toText(): String = joinToString(separator = "", prefix = "", postfix = "")

/**
 * Создаект FrequencyPair с помощью инфиксной функции
 *
 * Пример использования: 'a' to 0.2
 */
infix fun String.to(other: Int) = FrequencyPair(this, other)

/**
 * Свойство isPrefixed, вычисляющее является ли построенный
 * код префиксным
 */
val List<String>.isPrefixed: Boolean
    get() {
        forEachIndexed { i, item ->
            for (j in i + 1 until size)
                if (item == this[j].take(item.length))
                    return false
        }
        return this.isNotEmpty()
    }