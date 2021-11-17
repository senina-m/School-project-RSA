import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream

class Node(w: Int, left: Int, right: Int, used: Boolean, symb: Char, code: String)

class Compresing {
    var freq = mutableListOf<Int>() //len 256
    var tree = mutableListOf<Node>() //len 2*freq.size - 1
    var M: Int

    fun main(args: Array<String>) {
        val filename = "text.txt"
        val outFilename = "out.txt"
        freq = getfreq(filename)
        buildTree()
        encode(outFilename, filename)
    }

    private fun buildTree(): MutableList<Node> {
        M = 0
        var len = 0
        for (i in 0 until freq.size) {
            if (freq[i] != 0) {
                M++
                tree.add(Node(freq[i], -1, -1, false, i.toChar(), ""))
                len++
            }
        }

        if (M == 0) {
            println(message = "M is zero!")
        }

        for (i in 0 until 2 * M) {
            val min1 = findMin()
            tree[min1].used = true
            val min2 = findMin()
            tree[min2].used = true
            tree.add(Node(tree[min1].w + tree[min2].w, min1, min2, false, 1.toChar(), ""))
        }
        for (i in M..2 * M) {
            setCodes(i)
        }
        return tree
    }

    private fun findMin(): Int {
        var min = 1
        var i = 0
        while (i < tree.size) {
            if (freq[i] < min && !tree.used) {
                min = i
            }
            i++
        }
        return i
    }

    fun getfreq(filename: String): MutableList<Int> {
        val bin = BufferedInputStream(FileInputStream(filename))
        val f = mutableListOf<Int>()
        for (i in 0..256) {
            f.add(0)
        }
        var symp = bin.read()
        while (symp != -1) {
            f[symp] += 1
            val symp = bin.read()
        }
        bin.close()
        return f
    }

    fun setCodes(num: Int) {
        if (tree[num].symb >= 0) {
            return
        }
        tree[tree[num].left].code = tree[num].code + "0"
        tree[tree[num].right].code = tree[num].code + "1"
        setCodes(tree[num].left)
        setCodes(tree[num].right)
    }

    fun encode(outFilename: String, filename: String) {
        val bout = BufferedOutputStream(FileOutputStream(outFilename))
        bout.write(M - 1)

        for (i in 0..255) {
            if (freq[i] != 0) {
                bout.write(i)
                bout.write(freq[i] shr 24)
                bout.write(freq[i] shl 8 shr 24)
                bout.write(freq[i] shl 16 shr 24)
                bout.write(freq[i] shl 24 shr 24)
            }
        }

        val bin = BufferedInputStream(FileInputStream(filename))
        var s = bin.read()
        while (s != -1) {
            var buffer = ""
            buffer = buffer + codes[s]
            while (buffer.length >= 8) {
                val num = buffer.substring(0, 8)
                //            cut the buffer
                val x = Integer.parseInt(num, 2)
                bout.write(x)
            }
        }
    }

}