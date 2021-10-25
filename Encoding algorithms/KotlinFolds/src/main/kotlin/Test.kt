abstract class Test {
    abstract val name: String

    abstract fun test(): Boolean

    operator fun invoke(testsCount: Int) {
        outputHeader(name)
        val areTestsPassed =
            (1..testsCount).fold(true) { acc, _ ->
                acc && test()
            }
        outputResult(testsCount, areTestsPassed)
    }

    fun <T, R> output(
        ls: List<R>,
        res1: T,
        res2: T,
        name1: String = "Left ",
        name2: String = "Right",
        equal: String = "Equal"
    ): Boolean {
        val areEqual = res1 == res2
        println("List : $ls")
        println("$name1: $res1")
        println("$name2: $res2")
        println("$equal: $areEqual")
        println("------------------------------------------------------------")
        return areEqual
    }

    private fun outputHeader(name: String) {
        println()
        println("--------------------------- Тест ---------------------------")
        println("-- $name")
        println("------------------------------------------------------------")
    }

    private fun outputResult(testsCount: Int, areTestsPassed: Boolean) {
        println("Number of tests : $testsCount")
        println("All tests passed: $areTestsPassed")
        println("------------------------------------------------------------")
    }
}
