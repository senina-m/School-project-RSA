import java.io.BufferedInputStream
import java.io.FileInputStream
import java.util.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.log10


fun main(args: Array<String>) {
    val fileName = "RSA_simple_numbers.txt"
    val inputStream = BufferedInputStream(FileInputStream(fileName))
    val sc = Scanner(inputStream)
    sc.useDelimiter("[\\r\\n\\t ]+")
    val len = sc.nextInt()
    val numbers = IntArray(len)
    for (i in 0 until len) {
        numbers[i] = sc.nextInt()
    }
    sc.close()
    val random = Random()
    val max = len - 1
    val min = 20
    val p = numbers[random.nextInt(max - min + 1) + min].toLong()
    var ok = false
    var q = -1L
    while (!ok) {
        q = numbers[random.nextInt(max - min + 1) + min].toLong()
        if (p != q) {
            ok = true
        }
    }
    println(message = "Number p: $p")
    println(message = "Number q: $q")

    val n = p * q
    println(message = "Number N = p*q: $n")

    val phi = (p - 1) * (q - 1)
    println(message = "Phi(N): $phi")

    ok = false
    var e = -1L
    while (!ok) {
        e = numbers[random.nextInt(max - min + 1) + min].toLong()
        if (phi % e != 0L) {
            ok = true
        }
    }
    println(message = "Number e: $e")
    var d = euclid(e, phi).second.first
    if (d < 0) {
        d -= (-d / phi + 1) * phi
    }
    println(message = "Number d: $d")
    println(message = "Your private key (e,n): $e, $n")
    println(message = "Your public key (d,n): $d, $n")
    println(message = "Enter your message, please.")
    val scan = Scanner(System.`in`)
    val m = scan.nextLong()
    sc.close()
    var x: Long = -1
//    if (m % p != 0L && m % q != 0L) {
    x = pow(m, e, n)
//    }
    println("Secret message: $x")
    println("Your message was: " + pow(x, d, n))

}

fun getD(a: Long, b: Long): Long {
    var a1 = a
    var b1 = b
    var x1: Long = 1
    var y2: Long = 1
    var x2: Long = 0
    var y1: Long = 0
    var q: Long
    var t: Long

    while (b1 != 0L) {
        q = a1 / b1
        t = a1 % b1
        a1 = b1
        b1 = t

        t = x2
        x2 = x1 - q * x2
        x1 = t

        t = y2
        y2 = y1 - q * y2
        y1 = t
    }
    return x1
}

fun euclid(a: Long, b: Long): kotlin.Pair<Long, kotlin.Pair<Long, Long>> {
    if (a == 0L) {
        return kotlin.Pair(b, kotlin.Pair(0L, 1L))
    }
    val t = euclid(b % a, a)
    val g = t.first
    val x1 = t.second.first
    val y1 = t.second.second
    return kotlin.Pair(g, kotlin.Pair(y1 - (b / a) * x1, x1))
}

fun pow(a: Long, p: Long, n: Long): Long {
    var a1 = a
    a1 %= n
    return when {
        p == 0L -> 1
        p % 2 == 0L -> pow(a1 * a1 % n, p / 2, n)
        else -> a1 * pow(a1, p - 1, n) % n
    }
}

private fun getCountsOfDigits(number: Long): Int {
    return if (number == 0L) 1 else ceil(log10(abs(number) + 0.5)).toInt()
}