import kotlin.math.max
import kotlin.math.pow

// FIXME: Надо ли поддерживать отрицательные числа?
class Number {

    // Храним число разбитое на куски от младших разрядов к старшим
    // Для 1 234 567 890 будем хранить список 0:[890] 1:[567] 2:[234] 3: [  1]
    // Так будет удобнее делать операции и находить разряд по индексу
    private val chunks = mutableListOf<Long>()

    companion object {
        const val base  = 3
        val lBase = 10.toDouble().pow(this.base).toLong()
    }

    constructor(stringValue: String) {
        // 12345678 ==> 87654321 ==> 876 543 21 ==> [678] [345] [12]
        stringValue.reversed().chunked(base).forEach { chunk ->
            chunks.add(chunk.reversed().toLong())
        }
        // TODO: надо убрать ненужные нули из старших разрядов, если они есть
    }

    constructor(longValue: Long) {
        // 1234567  :
        // 1234     : [567]
        // 1        : [567] [234]
        // 0        : [567] [234] [  1]
        // reverse => [  1] [234] [567]
        var head = longValue
        while (head > 0) {
            chunks.add(head % lBase)
            head /= lBase
        }
    }

    override fun toString(): String {
        // Не забываем писать 0, если ничего нет - или же надо не забывать
        if (chunks.isEmpty()) return "0"
        val sb = java.lang.StringBuilder()
        // При составлении строки куски с большим индексом идут раньше, поэтому делаем reversed()!
        chunks.reversed().forEachIndexed { index, chunk ->
            if (index == 0) {
                // старшие разряды печатаем без нулей в начале
                sb.append("%d".format(chunk))
            }
            else {
                // в остальных дополняем впереди нулями до размера base
                sb.append("%0${base}d".format(chunk))
            }
        }
        return sb.toString()
    }

    // index считается с младшего разряда и начинается с 0!
    // 1234567, index=0 ==> 7
    // 1234567, index=1 ==> 6
    // 1234567, index=5 ==> 2
    fun getDigit(index: Int): Long {
        // 1234567 хранится как 0:[567] 1:[234] 2:[  1]
        // Если index=5, то нам нужен кусок 1 (начиная с 0) и разрад 2 (начиная с 0) внутри него
        val chunkIndex = index / base
        val indexInChunk = index % base
        return (chunks[chunkIndex] / 10.toDouble().pow(indexInChunk).toLong()) % 10
    }

    fun plus(num: Number): Number {
        // Складываем "в столбик": идём от младших разрядов к старшим,
        // пишем младшие base знаков суммы в теукщий разряд,
        // излишек откладываем для суммирования со следующими разрядами

        var extraPart = 0L // тут будем хранить излишек суммы
        for (index in 0 until max(chunks.size, num.chunks.size)) {
            val part1 = if (chunks.size > index) { chunks[index] } else { 0 }
            val part2 = if (num.chunks.size > index) { num.chunks[index] } else { 0 }
            val sum = part1 + part2 + extraPart
            val currentPart = sum % lBase
            if (chunks.size == index) {
                // Если очередного куска не было - добавляем
                chunks.add(currentPart)
            }
            else {
                // Если был - обновляем
                chunks[index] = currentPart
            }
            extraPart = sum / lBase
        }
        // Если что-то осталось для следующего куска - добавляем
        if (extraPart > 0) {
            chunks.add(extraPart)
        }
        // Т.к. сумма у нас сохранилась в текущем числе, возвращаем this, т.е. это самое число
        // TODO: Может быть, это не то, что нужно, и следует создавать новое число и возвращать его
        return this
    }

    fun normalize() {
        // TODO: Что это значит? Не про убирание ли это ненужных нулей?
    }
}
