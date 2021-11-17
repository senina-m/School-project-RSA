import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class DM(val div: Num, val mod: Num)

class Num {

    companion object {
        const val lbase = 1
        val base = 10.toDouble().pow(this.lbase).toLong()
    }

    var digits = mutableListOf<Long>()

    constructor(stringValue: String) {
        if (stringValue[0] == '-') {
            val str = stringValue.drop(1)
            for (i in 0 until str.length / lbase) {
                digits.add(
                    -str.substring(
                        str.length - lbase * (i + 1),
                        str.length - i * lbase
                    ).toLong()
                )
            }
            if (str.length % lbase != 0) {
                digits.add(-(str.substring(0, str.length % lbase)).toLong())
            }
        } else {
            for (i in 0 until stringValue.length / lbase) {
                digits.add(
                    stringValue.substring(
                        stringValue.length - lbase * (i + 1),
                        stringValue.length - i * lbase
                    ).toLong()
                )
            }
            if (stringValue.length % lbase != 0) {
                digits.add((stringValue.substring(0, stringValue.length % lbase)).toLong())
            }
        }
    }

    constructor(arr: List<Long>) {
        this.digits = arr.toMutableList()
    }

    constructor(long: Long) {
        var longValue = long
        if (long == 0.toLong()) {
            digits.add(0.toLong())
        } else {
            if (long < 0) {
                while (longValue < 0) {
                    digits.add((longValue % base))
                    longValue = (longValue / base)
                }
            } else {
                while (longValue > 0) {
                    digits.add(longValue % base)
                    longValue /= base
                }
            }
        }
    }

    fun getDigit(i: Int): Long {
        return if (i < digits.size) {
            digits[i]
        } else {
            0L
        }
    }

    override fun toString(): String {
//        this.normalize()
        if (digits.isEmpty() || (digits.size == 1 && digits[0] == 0.toLong())) return "0"
        val sb = StringBuilder()
        if (digits[digits.size - 1] < 0) {
            sb.append("-")
        }
        for (i in digits.size - 1 downTo 0 step 1) {
            val digit = digits[i]
            if ((abs(digit) >= 10.toDouble().pow(lbase - 1)) && (abs(digit) < 10.toDouble().pow(lbase)) ||
                (i == (digits.size - 1))
            ) {
                sb.append(abs(digit))
            } else {
                if (digit == 0.toLong()) {
                    sb.append("0".repeat(lbase))
                } else {
                    for (j in 1 until lbase) {
                        if ((abs(digit) < 10.toDouble().pow(j)) && (abs(digit) > 10.toDouble().pow(j - 1))) {
                            sb.append("0".repeat(lbase - i).plus(abs(digit).toString()))
                        }
                    }
                }
            }
        }
        return sb.toString()
    }

    private fun removeHeadZeros() {
        while (digits.size > 1 && digits[digits.size - 1] == 0L) {
            digits.removeAt(digits.size - 1)
        }
    }

    fun normalize() {
        if (digits.size == 1) {
            return
        } else {
            removeHeadZeros()
            for (i in 0 until digits.size) {
                val digit = getDigit(i)
                if (abs(digit) >= base) {
                    if (i == digits.size - 1) {
                        digits.add(0)
                    }
                    digits[i + 1] += digit / base
                    digits[i] = digit % base
                }
            }

            var positive = true
            if (digits[digits.size - 1] < 0) {
                positive = false
            }

            for (i in 0 until digits.size - 1) {
                val digit = getDigit(i)
                if (positive && digit < 0) {
                    digits[i] = base + digit
                    digits[i + 1] -= 1L
                }
                if (!positive && digit > 0) {
                    digits[i] = -base + digit
                    digits[i + 1] += 1L
                }
            }

            removeHeadZeros()
        }

    }

    fun sum(that: Num): Num {
        val arr = mutableListOf<Long>()
        for (i in 0 until max(that.digits.size, this.digits.size)) {
            arr.add(that.getDigit(i) + this.getDigit(i))
        }
        val a = Num(arr)
        a.normalize()
        return a
    }

    fun subtract(that: Num): Num {
        val new = mutableListOf<Long>()
        for (i in 0 until that.digits.size) {
            new.add(that.digits[i] * (-1))
        }
        return this.sum(Num(new))
    }

    private fun addNulls(k: Int) {
        for (i in 0 until k) {
            digits.add(0)
        }
    }

    private fun leftDigits(l: Int): Num {
        return if (digits.size >= l) {
            Num(digits.subList(0, l))
        } else {
            print("leftDigits out of bounds. l = $l, digits.size = ${digits.size}")
            Num(0)
        }
    }

    private fun rightDigits(l: Int): Num {
        return if (digits.size >= l) {
            Num(digits.subList(digits.size - l, digits.size))
        } else {
            print("rightDigits out of bounds. l = $l, digits.size = ${digits.size}")
            Num(0)
        }
    }

    private fun addNullsRight(l: Int): Num {
        val arr = mutableListOf<Long>()
        for (i in 0 until l) {
            arr.add(0.toLong())
        }
        for (i in 0 until digits.size) {
            arr.add(digits[i])
        }
        return Num(arr)
    }

    fun multiply(that: Num): Num {
        this.normalize()
        that.normalize()
        makeRightSize(that, this)
        val result = karatsuba(this, that)
        result.normalize()
        return result
    }

    private fun makeRightSize(first: Num, second: Num) {
        if (first.digits.size > second.digits.size) {
            second.addNulls(first.digits.size - second.digits.size)
        } else if (first.digits.size < second.digits.size) {
            first.addNulls(second.digits.size - first.digits.size)
        }
        if (first.digits.size % 2 == 1) {
            second.addNulls(1)
            first.addNulls(1)
        }
    }

    private fun karatsuba(first: Num, second: Num): Num {
        first.normalize()
        second.normalize()
        if (min(first.digits.size, second.digits.size) == 1) {
            if (first.digits.size == 1) {
                val arr = mutableListOf<Long>()
                for (i in 0 until second.digits.size) {
                    arr.add(second.digits[i] * first.digits[0])
                }
                return Num(arr)
            } else if (second.digits.size == 1) {
                val arr = mutableListOf<Long>()
                for (i in 0 until first.digits.size) {
                    arr.add(first.digits[i] * second.digits[0])
                }
                return Num(arr)
            }
        }
        makeRightSize(first, second)

        val x1 = first.rightDigits(first.digits.size / 2)
        val x2 = first.leftDigits(first.digits.size / 2)
        val y1 = second.rightDigits(second.digits.size / 2)
        val y2 = second.leftDigits(second.digits.size / 2)
        val a1 = karatsuba(x1, y1)
        val a2 = karatsuba(x2, y2)
        val a3 = karatsuba(x1 + x2, y1 + y2)
        val c2 = a3 - (a1 + a2)
        return ((a1.addNullsRight(first.digits.size)) + (c2.addNullsRight(first.digits.size / 2))) + (a2)
    }


    fun divide(that: Num): DM {
        this.normalize()
        that.normalize()
        return if (that > this) {
            DM(Num(0.toLong()), this)
        } else {
            val result = mutableListOf<Long>()
            val arr = mutableListOf<Long>()
            for (i in this.digits.size - that.digits.size until this.digits.size) {
                arr.add(this.digits[i])
            }
            val a = Num(arr)
            val b = Num(that.digits)
            var r = Num(0L)
            for (i in this.digits.size - that.digits.size - 1 downTo -1) {
                val q = binarySearchDiv(a, b)
                result.add(q)
                r = a - (b * (Num(q)))
                if (i >= 0) {
                    a.digits = mutableListOf(this.digits[i])
                    a.digits.addAll(r.digits)
                }
            }
            val m = Num(result.reversed())
            m.normalize()
            r.normalize()
            DM(m, r)
        }
    }

    private fun binarySearchDiv(a: Num, b: Num): Long {
        var i = 0.toLong()
        var j = base // - 1.toLong()
        var q = j / 2
        while (true) {
            val r = a - b * Num(q)
            if (b > r  && r >= Num(0L)) {
                break
            } else {
                if (r >= Num(0L)) {
                    i = q
                } else {
                    j = q
                }
                q = (j + i) / 2
            }
        }
        return q
    }

    private fun grater(that: Num): Boolean {
        val k = this - that
        k.normalize()
        return k.digits[k.digits.size - 1] > 0
    }

    fun absNum(): Num {
        normalize()
        val new: MutableList<Long> = mutableListOf<Long>()
        for (i in 0 until digits.size) {
            new.add(abs(digits[i]))
        }
        return Num(new)
    }

    private fun smaller(that: Num): Boolean {
        return that.grater(this)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Num || this.digits != other.digits) {
            return false
        }
        return true
    }

    operator fun compareTo(other: Num): Int = when {
        this.grater(other) -> 1
        this.smaller(other) -> -1
        else -> 0
    }

    operator fun plus(that: Num): Num = this.sum(that)
    operator fun minus(that: Num): Num = this.subtract(that)
    operator fun times(that: Num): Num = this.multiply(that)
    operator fun div(that: Num): Num = this.divide(that).div
    operator fun rem(that: Num): Num = this.divide(that).mod

    operator fun plus(that: Int): Num = this.sum(Num(that.toLong()))
    operator fun minus(that: Int): Num = this.subtract(Num(that.toLong()))
    operator fun times(that: Int): Num = this.multiply(Num(that.toLong()))
    operator fun div(that: Int): Num = this.divide(Num(that.toLong())).div
    operator fun rem(that: Int): Num = this.divide(Num(that.toLong())).mod

    operator fun plus(that: Long): Num = this.sum(Num(that))
    operator fun minus(that: Long): Num = this.subtract(Num(that))
    operator fun times(that: Long): Num = this.multiply(Num(that))
    operator fun div(that: Long): Num = this.divide(Num(that)).div
    operator fun rem(that: Long): Num = this.divide(Num(that)).mod
}

operator fun Long.plus(that: Num): Num = Num(this) + that
operator fun Int.plus(that: Num): Num = Num(this.toLong()) + that
operator fun Long.minus(that: Num): Num = Num(this) - that
operator fun Int.minus(that: Num): Num = Num(this.toLong()) - that
operator fun Long.times(that: Num): Num = Num(this) * that
operator fun Int.times(that: Num): Num = Num(this.toLong()) * that
operator fun Long.div(that: Num): Num = Num(this) / that
operator fun Int.div(that: Num): Num = Num(this.toLong()) / that
operator fun Long.rem(that: Num): Num = Num(this) % that
operator fun Int.rem(that: Num): Num = Num(this.toLong()) % that
