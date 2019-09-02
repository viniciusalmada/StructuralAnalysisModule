package utils

import vsca.doublematrix.lib.DoubleMatrix
import java.security.InvalidParameterException
import java.util.*

fun DoubleArray.showAsNodeDirections(title: String) {
    val out = StringBuilder()

    var init = 1
    var node = 1
    println("------$title------")
    this.forEachIndexed { i, value ->
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

fun DoubleArray.subVectorFromIncidence(incidence: IntArray): DoubleArray {
    val res = DoubleArray(incidence.size)
    incidence.forEachIndexed { index, i -> res[index] = this[i - 1] }
    return res
}

inline fun IntArray.forEachIncidenceToMatrix(block: (Int, Int, Int, Int) -> Unit) {
    this.forEachIndexed { row, valueRow ->
        this.forEachIndexed { column, valueCol ->
            block(row, column, valueRow, valueCol)
        }
    }
}

inline fun IntArray.forEachIncidenceToVector(block: (Int, Int) -> Unit) {
    this.forEachIndexed { row, valueRow ->
        block(row, valueRow)
    }
}

operator fun DoubleArray.times(d: Double): DoubleArray {
    val dArray = DoubleArray(this.size)
    this.forEachIndexed { index, value -> dArray[index] = value * d }
    return dArray
}

operator fun DoubleMatrix.times(vector: DoubleArray): DoubleArray {
    if (this.cols != vector.size) throw InvalidParameterException("Dimensions not agree")

    val res = DoubleArray(vector.size)
    this.forEachRowColumn { row, col, value ->
        res[row] += value * vector[col]
    }

    return res
}

operator fun DoubleArray.minus(vector: DoubleArray): DoubleArray {
    val res = DoubleArray(this.size)
    this.forEachIndexed { index, d -> res[index] = d - vector[index] }
    return res
}

operator fun DoubleArray.plus(vector: DoubleArray): DoubleArray {
    val res = DoubleArray(this.size)
    this.forEachIndexed { index, d -> res[index] = d + vector[index] }
    return res
}