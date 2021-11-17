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
    var p = Num(numbers[random.nextInt(max - min + 1) + min].toLong())
//    val p = Num(1153L)
    var ok = false
    var q = Num(-1L)
    while (!ok) {
        q = Num(numbers[random.nextInt(max - min + 1) + min].toLong())
        if (p != q) {
            ok = true
        }
    }
//    q = Num(2377L)
    println(message = "Number p: $p")
    println(message = "Number q: $q")

    val n = p * q
    println(message = "Number N = p*q: $n")

    var phi = (p - 1) * (q - 1)
    println(message = "Phi(N): $phi")

    ok = false
    var e = Num(-1L)
    while (!ok) {
        e = Num(numbers[random.nextInt(max - min + 1) + min].toLong())
        if (phi % e != Num(0L)) {
            ok = true
        }
    }
    println(message = "Number e: $e")
    val euclid = euclid(e, phi)
    var d = euclid.second.first
    var y = euclid.second.second
    p = Num(1931L)
    q = Num(601L)
    e = Num(947L)
    d = Num(358283L)


    p = Num(2099L)
    q = Num(1327L)
    e = Num(2593L)
    d = Num(391597L)
//    Your private key (e,n): 2593, 2785373
//    Your public key (d,n): 391597, 2785373
    if (d < Num(0)) {
        d += (Num(0) - d / phi + 1) * phi
        y -= (Num(0) - d / phi + 1) * e
    }
    println(message = "Number e * d + phi * y = ${e * d + phi * y}")
    println(message = "Number d: $d")
    println(message = "Your private key (e,n): $e, ${p*q}")
    println(message = "Your public key (d,n): $d, ${p*q}")
    println(message = "Enter your message, please.")
    val scan = Scanner(System.`in`)
    val m = Num(scan.nextLong())
    sc.close()
    var x = Num(-1)
    if (m % p != Num(0L) && m % q != Num(0L)) {
        x = pow(m, e, n)
    }
    println("Secret message: $x")
    println("Your message was: " + pow(x, d, n))
}


fun euclid(a: Num, b: Num): kotlin.Pair<Num, kotlin.Pair<Num, Num>> {
    if (a == Num(0L)) {
        return kotlin.Pair(b, kotlin.Pair(Num(0L), Num(1L)))
    }
    val t = euclid(b % a, a)
    val g = t.first
    val x1 = t.second.first
    val y1 = t.second.second
    return kotlin.Pair(g, kotlin.Pair(y1 - ((b / a) * x1), x1))
}


public fun pow(a: Num, p: Num, n: Num): Num {
    var a1 = a
    a1 %= n
    return when {
        p == Num(0L) -> Num(1L)
        p % 2 == Num(0L) -> pow(a1 * a1 % n, p / 2, n)
        else -> a1 * pow(a1, p - 1, n) % n
    }
}

private fun getCountsOfDigits(number: Long): Int {
    return if (number == 0L) 1 else ceil(log10(abs(number) + 0.5)).toInt()
}