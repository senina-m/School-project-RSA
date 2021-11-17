import org.junit.Test
import kotlin.test.assertEquals

class NumberTest {
    @Test
    fun createFromString() {
        val stringValue = "12345678"
        val Num = Num(stringValue)
        stringValue.reversed().forEachIndexed { index, char ->
            assertEquals(char.toString().toLong(), Num.getDigit(index))
        }
        assertEquals(stringValue, Num.toString())
    }


    @Test
    fun createFromLong() {
        val longValue = 12345678L
        val Num = Num(longValue)
        longValue.toString().reversed().forEachIndexed { index, char ->
            assertEquals(char.toString().toLong(), Num.getDigit(index))
        }
        assertEquals(longValue.toString(), Num.toString())
    }

    @Test
    fun checkToString() {
        assertEquals("0", Num(0).toString())
        assertEquals("10", Num(10).toString())
        assertEquals("100", Num(100).toString())
        assertEquals("1000", Num(1000).toString())
        assertEquals("1000234", Num(1000234).toString())
    }


    @Test
    fun sum() {
        var number1 = Num("999")
        var number2 = Num("999")
        var sum = number1.sum(number2)
        assertEquals((999 + 999).toString(), sum.toString())

        number1 = Num("1")
        number2 = Num("999")
        sum = number1.sum(number2)
        assertEquals((1 + 999).toString(), sum.toString())

        number1 = Num("101")
        number2 = Num("9090909")
        sum = number1.sum(number2)
        assertEquals((101 + 9090909).toString(), sum.toString())
    }

    @Test
    fun multiply() {
        var number1 = Num("300")
        var number2 = Num("13")
        var res = number1.multiply(number2)
        assertEquals((300L * 13L).toString(), res.toString())

        number1 = Num("1")
        number2 = Num("999")
        res = number1.multiply(number2)
        assertEquals((1L * 999L).toString(), res.toString())

        number1 = Num("4383")
        number2 = Num("90")
        res = number1.multiply(number2)
        assertEquals((4383L * 90L).toString(), res.toString())

        number1 = Num("121242342")
        number2 = Num("1637")
        res = number1.multiply(number2)
        assertEquals((121242342L * 1637L).toString(), res.toString())
    }

    @Test
    fun divide() {
        var number1 = Num("566")
        var number2 = Num("239")
        var res = number1.divide(number2).div
        assertEquals((566L / 239L).toString(), res.toString())

        number1 = Num("1")
        number2 = Num("999")
        res = number1 % (number2)
        assertEquals((1L % 999L).toString(), res.toString())

        number1 = Num("566")
        number2 = Num("239")
        res = number1 % (number2)
        assertEquals((566L % 239L).toString(), res.toString())

        number1 = Num("1")
        number2 = Num("999")
        res = number1.divide(number2).div
        assertEquals((1L / 999L).toString(), res.toString())

        number1 = Num("121242342")
        number2 = Num("1637")
        res = number1.divide(number2).div
        assertEquals((121242342L / 1637L).toString(), res.toString())

        number1 = Num("121242342")
        number2 = Num("1637")
        res = number1 / number2
        assertEquals((121242342L / 1637L).toString(), res.toString())

        number1 = Num("121242342")
        number2 = Num("1637")
        res = number1 % number2
        assertEquals((121242342L % 1637L).toString(), res.toString())

        number1 = Num("21")
        number2 = Num("2099")
        res = number1 % number2
        assertEquals((21L % 2099L).toString(), res.toString())

        number1 = Num("0")
        number2 = Num("709")
        res = number1 / number2
        assertEquals((0L / 709L).toString(), res.toString())
        res = number1 % number2
        assertEquals((0L % 709L).toString(), res.toString())

        number1 = Num("648240")
        number2 = Num("709")
        res = number1 / number2
        assertEquals((648240L / 709L).toString(), res.toString())
        res = number1 % number2
        assertEquals((648240L % 709L).toString(), res.toString())
    }
}