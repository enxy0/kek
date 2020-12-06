import kotlin.math.abs

/**
 * ЛАБОРАТОРНАЯ РАБОТА 9.
 * ЯЗЫК Kotlin: законы объединения для свёрток
 *   - законы fold-объединения;
 *   - законы fold-map-объединения;
 *   - законы foldr-concat-объединения, foldr-map-concat-объединения;
 *   - законы fold-scan-объединения.
 *
 * Автор: Бушуев Никита Федорович
 * Дата: 06.12.2020
 * Время выполнения: ~4ч.
 * Компилятор: Kotlin 1.4.20 (JVM)
 */

fun main() {
    Theorem11(5)
    Theorem12(20)
    Theorem21(5)
    Theorem22(5)
    Theorem31(5)
    Theorem32(5)
    Theorem41(5)
    Lemma4(5)
    Theorem42(5)
}

/**
 * Теорема 1.1 (закон объединения для foldr) [Bird,1998,p.131].
 * ┌─────────────────────────┐
 * │f . foldr g a = foldr h b│
 * └─────────────────────────┘
 */
object Theorem11 : Test() {
    override val name = "Теорема 2.1 (foldr-map объединение) [Bird,1998,p.132; Gibbons,2003,p.43]."

    override fun test(): Boolean {
        val f: (Int) -> Int = { x -> x * 2 }
        val h: (Int, Int) -> Int = { x, y -> x * 2 + y }
        val g: (Int, Int) -> Int = Int::plus
        val a = 2
        val b = 4
        val ls = generateList(size = (1..10).random())

        val res1 = f(foldr(g, a, ls))
        val res2 = foldr(h, b, ls)

        output(ls, res1, res2) // красиво выводим результат

        return res1 == res2
    }
}

/**
 * TODO: Нужно подобрать функцию, удовлетворяющую условию. Сейчас работает неправильно!!!
 * Теорема 1.1 (закон объединения для foldr) [Bird,1998,p.131].
 * ┌─────────────────────────┐
 * │f . foldr g a = foldr h b│
 * └─────────────────────────┘
 */
object Theorem12 : Test() {
    override val name = "Теорема 12 [Bird,1998,p.131] (закон объединения для foldl)."

    override fun test(): Boolean {
        val f: (Int) -> Int = { x -> x * 2 }
        val h: (Int, Int) -> Int = { x, y -> x * 2 + y }
        val g: (Int, Int) -> Int = Int::plus
        val a = 2
        val b = 4
        val ls = generateList(size = (1..10).random())

        val res1 = f(foldl(g, a, ls))
        val res2 = foldl(h, b, ls)

        output(ls, res1, res2) // красиво выводим результат

        return res1 == res2
    }
}

/**
 * Теорема 2.1 (foldr-map объединение) [Bird,1998,p.132; Gibbons,2003,p.43].
 * ┌───────────────────────────────────┐
 * │foldr f e . map g = foldr (f . g) e│
 * └───────────────────────────────────┘
 */
object Theorem21 : Test() {
    override val name = "Теорема 2.1 (foldr-map объединение) [Bird,1998,p.132; Gibbons,2003,p.43]."

    override fun test(): Boolean {
        val f: (Int, Int) -> Int = Int::minus
        val g: (Int) -> Int = ::abs
        val e = 0
        val ls = generateList()

        val res1 = foldr(f, e, map(g, ls))
        val res2 = foldr(f compose g, e, ls)

        output(ls, res1, res2) // красиво выводим результат

        return res1 == res2
    }
}

/**
 * Теорема 2.2 (foldl-map объединение) [Bird,Wadler,1988,p.72].
 * ┌─────────────────────────────────┐
 * │foldl (Х) e . map f = foldl (х) e│
 * └─────────────────────────────────┘
 */
object Theorem22 : Test() {
    override val name = "Теорема 22 (foldl-map объединение) [Bird,Wadler,1988,p.72]."

    override fun test(): Boolean {
        val g1: (Int, Int) -> Int = Int::minus
        val f: (Int) -> Int = ::abs
        val e = 0
        val ls = generateList()

        val res1 = foldl(g1, e, map(f, ls))
        val res2 = foldl({ x, y -> g1(x, f(y)) }, e, ls)

        output(ls, res1, res2) // красиво выводим результат

        return res1 == res2
    }
}

/**
 * Теорема 3.1 (foldr-concat объединение) [Bird,1998,p.132].
 * ┌─────────────────────────────────────────────┐
 * │foldr f e . concat = foldr (flip (foldr f)) e│
 * └─────────────────────────────────────────────┘
 */
object Theorem31 : Test() {
    override val name = "Теорема 3.1 (foldr-concat объединение) [Bird,1998,p.132]."

    override fun test(): Boolean {
        val f: (Int, Int) -> Int = Int::times // или так: val f = { x: Int, y: Int -> x + y }
        val e = 0
        val ls = generateLists()

        val res1 = foldr(f, e, concat(ls)) // левая часть выражения
        val res2 = foldr({ list, acc -> foldr(f, acc, list) }, e, ls) // правая часть выражения

        output(ls, res1, res2) // красиво выводим результат

        return res1 == res2
    }
}

/**
 *  Теорема 3.2 (foldr-map-concat объединение) [Bird,1998,p.132-133].
 * ┌────────────────────────────────────────────────┐
 * │foldr f e . concat = foldr f e . map (foldr f e)│.
 * └────────────────────────────────────────────────┘
 */
object Theorem32 : Test() {
    override val name = "Теорема 3.2 (foldr-map-concat объединение) [Bird,1998,p.132-133]."

    override fun test(): Boolean {
        val f: (Int, Int) -> Int = Int::plus
        val e = 0
        val ls = generateLists()

        val res1 = foldr(f, e, concat(ls))
        val res2 = foldr(f, e, map({ list -> foldr(f, e, list) }, ls))

        output(ls, res1, res2)

        return res1 == res2
    }
}

/**
 *  Теорема 4.1 (foldr-scan объединение) (по [Bird,1998,с.133-134]).
 * ┌──────────────────────────────────────┐
 * │foldr1 (Х) . scanl (х) e = foldr (*) e│
 * └──────────────────────────────────────┘
 */
object Theorem41 : Test() {
    override val name = "Теорема 4.1 (foldr-scan объединение) (по [Bird,1998,с.133-134])."

    override fun test(): Boolean {
        val f: (Int, Int) -> Int = Int::plus
        val g: (Int, Int) -> Int = Int::times
        val e = 1
        val ls = generateList()

        val res1 = foldr1(f, scanl(g, e, ls))
        val res2 = foldr({ x, y -> f(e, (g(x, y))) }, e, ls)

        output(ls, res1, res2)

        return res1 == res2
    }
}

/**
 * Лемма (fold-scan-объединение) (по [Bird,1998,с.133]).
 *
 * scanl f e = foldr g [e]
 *
 * Если f - ассоциативная операция, e - единица f, то
 * scanl f e = foldr g [e], где g x y = e : map (f x) y.
 *
 */
object Lemma4 : Test() {
    override val name = "Лемма (fold-scan-объединение) (по [Bird,1998,с.133])."

    override fun test(): Boolean {
        val f: (Int, Int) -> Int = Int::plus
        val e = 1
        val ls = generateList((1..20).random())

        val res1 = scanl(f, e, ls)
        val res2 = foldr({ x, y -> listOf(e) + map({ f(x, it) }, y) }, listOf(e), ls)

        output(ls, res1, res2)

        return res1 == res2
    }
}

/**
 * Теорема 4.2 (foldl-scan объединение) [Bird,1998,p.137, формулировка исправлена)].
 * ┌────────────────────────────────────────────────┐
 * │foldl1 (Х) . scanl (х) e = fst . foldl (*) (e,e)│.
 * └────────────────────────────────────────────────┘
 */
object Theorem42 : Test() {
    override val name = "Теорема 4.2 (foldl-scan объединение) [Bird,1998,p.137, формулировка исправлена)]."

    override fun test(): Boolean {
        val f: (Int, Int) -> Int = Int::times
        val g: (Int, Int) -> Int = Int::minus
        val e = 20
        val ls = generateList((1..100).random())

        val res1 = foldl1(f, scanl(g, e, ls))
        val res2 = fst(foldl({ (x, y), z -> listOf(f(x, (g(y, z))), g(y, z)) }, listOf(e, e), ls))

        output(ls, res1, res2)

        return res1 == res2
    }
}

/************************************************************
 * Псевдо-рандомные генераторы
 ************************************************************/

// Генерация списка списков псевдо-рандомных чисел
fun generateLists(
    size: IntRange = ((1..2).random()..(3..5).random())
): List<List<Int>> =
    size.fold(mutableListOf()) { acc, _ ->
        acc.apply { add(generateList()) }
    }

// Генерация списка псевдо-рандомных чисел
fun generateList(size: Int = (1..10).random()): List<Int> =
    (1..size).fold(mutableListOf()) { acc, i ->
        acc.apply { add((1..10).random() % (i + 1)) }
    }

/************************************************************
 * Реализации функций fold, map, concat... из Haskell
 ************************************************************/

// Реализация функции foldr из Haskell
fun <T, R> foldr(f: (T, R) -> R, init: R, list: List<T>): R =
    list.foldRight(init, f)

// Реализация функции foldl из Haskell
fun <T, R> foldl(f: (R, T) -> R, init: R, list: List<T>): R =
    list.fold(init, f)

// Реализация функции concat из Haskell
fun <T> concat(list: List<List<T>>): List<T> = list.flatten()

// Реализация функции map из Haskell
fun <T, R> map(f: (T) -> R, list: List<T>): List<R> = list.map(f)

fun <T, R> scanl(f: (R, T) -> R, init: R, list: List<T>): List<R> = list.runningFold(init, f)

// Реализация функции foldr1 из Haskell
fun <T> foldl1(f: (T, T) -> T, list: List<T>): T =
    list.reduce(f)

// Реализация функции foldl1 из Haskell
fun <T> foldr1(f: (T, T) -> T, list: List<T>): T =
    list.reduceRight(f)

// Первый элемент списка
fun <T> fst(list: List<T>): T = list.first()

// Композиция функции двух функций с одним аргументом
infix fun <T, R, V> ((T) -> R).compose(other: (R) -> V): (T) -> V = {
    other(this(it))
}

// Композиция функции с двумя аргументами и функции с одним аргументом с одинаковыми типами
infix fun <T> ((T, T) -> T).compose(other: (T) -> T): (T, T) -> T = { first, second ->
    this(other(first), second)
}
