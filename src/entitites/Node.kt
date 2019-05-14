package entitites

import utils.SupportCondition
import utils.SupportCondition.FREE
import utils.SupportCondition.SPRING
import vsca.doublematrix.lib.DoubleMatrix
import java.util.*

@Suppress("UnnecessaryVariable")
class Node(
	val id: Int,
	val x: Double,
	val y: Double,
	val supportCondition: Array<SupportCondition>,
	val supportValues: DoubleArray
) {
	constructor(
		id: Int,
		x: Double,
		y: Double,
		supportCondition: Array<SupportCondition>
	) : this(id, x, y, supportCondition, doubleArrayOf(0.0, 0.0, 0.0))
	
	constructor(
		id: Int,
		x: Double,
		y: Double
	) : this(id, x, y, arrayOf(FREE, FREE, FREE))
	
	fun calculateStiffnessMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val K = DoubleMatrix(degreeOfFreedom)
		supportCondition.forEachIndexed { index, supportCondition ->
			if (supportCondition == SPRING) {
				val k = supportValues[index]
				/*
				index 0 -> direção 1 -> 3*id-2
				index 1 -> direção 2 -> 3*id-1
				index 2 -> direção 3 -> 3*id
				 */
				val i = 3 * id - (2 - index)
				K[i - 1, i - 1] += k
			}
		}
		return K
	}
	
	
	/*fun calculateKnownDisplacementsVector(degreeOfFreedom: Int): DoubleMatrix {
		var directionsOfFreedom = DoubleMatrix(3,1)
		this.supportCondition.forEachIndexed { index, supportCondition ->
			if (supportCondition == FIX){
				directionsOfFreedom
			}
		}
		TODO()
	}*/
	
	fun calculateGlobalLoadVector(degreeOfFreedom: Int): DoubleMatrix {
		val B = calculateIncidenceMatrix(degreeOfFreedom)
		val f = calculateLocalLoadVector()
		val P = B.transpose() * f
		return P
	}
	
	private fun calculateIncidenceMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val B = DoubleMatrix(3, degreeOfFreedom)
		val e = intArrayOf(3 * id - 2, 3 * id - 1, 3 * id)
		for (i in 1..3)
			B[i - 1, e[i - 1] - 1] = 1.0
		
		return B
	}
	
	private fun calculateLocalLoadVector(): DoubleMatrix {
		val f = DoubleMatrix(3, 1)
		supportCondition.forEachIndexed { index, supportCondition ->
			if (supportCondition == FREE && supportValues[index] != 0.0) {
				f[index, 0] = supportValues[index]
			}
		}
		return f
	}
	
	fun getDirections(): IntArray {
		return intArrayOf(
			3 * id - 2,
			3 * id - 1,
			3 * id
		)
	}
	
	override fun toString(): String {
		val node = StringBuilder()
		node.append("entitites.Node $id\t")
		node.append("${Arrays.toString(supportCondition)}\t")
		node.append("${Arrays.toString(supportValues)}\t")
		return node.toString()
	}
	
	override fun hashCode(): Int {
		return id
	}
	
	override fun equals(other: Any?): Boolean {
		return this.id == (other as Node).id
	}
	
}