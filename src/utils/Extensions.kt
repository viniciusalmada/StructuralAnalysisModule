package utils

import vsca.doublematrix.lib.DoubleMatrix
import java.util.*

fun DoubleMatrix.showAsNodeDirections(title: String) {
    val out = StringBuilder()

    var init = 1
    var node = 1
	println("------$title------")
	this.forEachRowColumn { i, _, value ->
        if (i != init) {
            out.append("[")
            out.append(" ")
            out.append("]\t[")
        } else {
            out.append("[")
            out.append("${node++}")
            out.append("]\t[")
            init += 3
        }
        val valueString = when {
            value < 0 -> "%.4f".format(Locale.ENGLISH, value)
            value > 0 -> "+%.4f".format(Locale.ENGLISH, value)
            else -> " %.4f".format(Locale.ENGLISH, value)
        }
        out.append("$valueString]\n")
        if (init == i + 2)
            out.append("-------\n")
    }
    println(out)
}