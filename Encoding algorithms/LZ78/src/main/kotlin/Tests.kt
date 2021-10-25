fun runRandomTests(count: Int = 10) {
    testHeader("LZ78")
    val areTestsCorrect = (0..count).fold(true) { acc, number ->
        acc && test(
            input = generateString(),
            encoder = LZ78::encode,
            decoder = LZ78::decode,
            testNumber = number
        )
    }
    testResult(areTestsCorrect)
}

fun <T> test(
    input: T,
    encoder: (T) -> LZ78.Result,
    decoder: (LZ78.Result) -> T,
    testNumber: Int
): Boolean {
    val result = encoder(input)
    val decoded = decoder(encoder(input))
    val isCorrect = decoded == input
    println("Test №$testNumber")
    println("Input    = \"$input\"")
    println("Decoded  = \"$decoded\"")
    println("Codes    = ${result.codes}")
    println("Result   = ${if (isCorrect) "passed" else "error"}")
    println("--------------------------------------------------------------")
    return isCorrect
}

fun testHeader(header: String) {
    println()
    println()
    println("---------------------- Running tests on ----------------------")
    println("Algorithm: $header")
    println("--------------------------------------------------------------")
}

fun testResult(areTestsCorrect: Boolean) {
    println("All tests passed: $areTestsCorrect")
    println("--------------------------------------------------------------")
}

fun generateString() = (0..(0..10).random()).fold("") { acc, length ->
    acc +
        ('a'..'z').random().toString().repeat(length) +
        ('a'..'z').random().toString().repeat(length % 10) +
        ('a'..'z').random().toString().repeat(length % 10 % 2) +
        ('a'..'z').random().toString().repeat(length % 10 % 3)
}
