fun main() {
    println(" 15(10) = 10011(-2): ${"15" == 10011.fromNega() && "10011" == 15.toNega()}")
    println("-15(10) = 110001(-2): ${"-15" == 110001.fromNega() && "110001" == (-15).toNega()}")
}

fun Int.toNega(): String {
    val mask = 0xAAAAAAAAAAAAAAA
    return ((this + mask) xor mask).toString(radix = 2)
}

fun Int.fromNega(): String {
    val negaBinary = this.toString(radix = 10).toInt(radix = 2)
    val mask = 0b1010101010101010
    return ((negaBinary xor mask) - mask).toString(radix = 10)
}