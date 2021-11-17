import org.junit.Test
import kotlin.math.pow
import kotlin.test.assertEquals

class RSATest {
    @Test
    fun rsa_euclid() {
        var phi = 23L
        var e = 41L
        var d = euclid(e, phi).second.first
        var f = euclid(e, phi).second.second
        assertEquals(1L, d * e + phi * f)

        phi = 174636L
        e = 2473L
        d = euclid(e, phi).second.first
        f = euclid(e, phi).second.second
        assertEquals(d * e + phi * f, 1)
    }

    @Test
    fun rsaLong_euclid() {
        val p = Num(2221L)
        val q = Num(293L)
        var phi = (p - 1) * (q - 1)  // Num(648240L)
        var e = Num(709L)
        val x = phi % e
        println(x)
        var (d, f) = euclid(e, phi).second
        assertEquals(d * e + phi * f, Num(1))

        phi = Num(1066240L)
        e = Num(1039L)
        d = euclid(e, phi).second.first
        f = euclid(e, phi).second.second
        if (d < Num(0)) {
            d += (Num(0) - d / phi + 1) * phi
            f -= (Num(0) - d / phi + 1) * e
        }
        assertEquals(d * e + phi * f, Num(1L))
    }

    @Test
    fun longRsa_pow(){
        var m = Num(97L)
        var e = Num(809L)
        var n = Num(3141791L)
        var d = Num(1477745L)
        assertEquals(Num((97.toDouble().pow(809.toDouble())).toLong() % 3141791L), pow(m, e, n))
    }

    @Test
    fun rsa_pow(){
        var m = 97L
        var e =809L
        var n =3141791L
        var d = 1477745L
        assertEquals((97.toDouble().pow(809.toDouble())).toLong() % 3141791L, pow(m, e, n))
    }

//    @Test
//    fun d(){
//        var e = Num(503L)
//        var d = Num(8567L)
//        var n =  Num(241147L)
//        var m =  Num(1232L)
//        var p = Num(1597L)
//        var q = Num(151L)
//        var x = Num(-1)
//        if (m % p != Num(0L) && m % q != Num(0L)) {
//            x = pow(m, e, n)
//        }
//        println("Secret message: $x")
//        println("Your message was: " + pow(x, d, n))
//        assertEquals(pow(x, d, n), Num(-1))
//    }
}