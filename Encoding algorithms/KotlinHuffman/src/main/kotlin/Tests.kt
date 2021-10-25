fun runTests(times: Int = 15) {
    testHeader("Huffman", times)
    repeat(times) {
        test(
            input = randomLetterSequence(),
            encoder = Huffman::encode,
            decoder = Huffman::decode
        )
    }
}

fun <T> test(
    input: T,
    encoder: (T) -> Huffman.Result,
    decoder: (Huffman.Result) -> T
): Boolean {
    val result = encoder(input)
    val isPrefixed = result.codes.values.toList().isPrefixed
    val isCorrect = isPrefixed && decoder(result) == input
    println("Input    = ${if (input is String) "\"$input\"" else input.toString()}")
    println("Encoded  = ${result.encoded}")
    println("Codes    = ${result.codes}")
    println("Prefixed = ${if (isPrefixed) "yes" else "no"}")
    println("Result   = ${if (isCorrect) "passed" else "error"}")
    println("--------------------------------------------------------------")
    return isCorrect
}

fun testHeader(header: String, times: Int) {
    println()
    println()
    println("---------------------- Running tests on ----------------------")
    println("Algorithm: $header")
    println("Number of tests: $times")
    println("--------------------------------------------------------------")
}

fun randomLetterSequence(): String {
    fun stringOfRandomLetter(times: Int = 1): String {
        val random = ('a'..'z').random().toString().repeat((1..10).random())

        return if (times == 0) {
            random
        } else {
            random + stringOfRandomLetter(times - 1)
        }
    }

    return stringOfRandomLetter((2..10).random())
}
