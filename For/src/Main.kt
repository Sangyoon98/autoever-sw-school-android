fun main() {
    val k1 = 1 .. 10
    println(k1.toList())

    val k2 = 1 until 10
    println(k2.toList())

    val k3 = 1 ..< 10
    println(k3.toList())

    val k4 = 1 .. 10 step 2
    println(k4.toList())

    val k5 = 10 downTo 1 step 2
    println(k5.toList())
}