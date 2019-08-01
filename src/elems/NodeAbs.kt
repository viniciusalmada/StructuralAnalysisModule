package elems

import utils.SupportCondition
import vsca.doublematrix.lib.DoubleMatrix

abstract class NodeAbs(
		val mId: Int,
		val mCoordX: Double,
		val mCoordY: Double,
		val mCoordZ: Double,
		suppCondStrings: Array<String>,
		val mLoadValues: DoubleArray,
		val mStiffValues: DoubleArray
) {
	val mSupportCondition = ArrayList<SupportCondition>()
	
	init {
		for (sup in suppCondStrings) {
			when (sup) {
				"FIX" -> mSupportCondition.add(SupportCondition.FIX)
				"FREE" -> mSupportCondition.add(SupportCondition.FREE)
				"SPRING" -> mSupportCondition.add(SupportCondition.SPRING)
			}
		}
	}
	
	abstract fun calculateIncidenceMatrix(degreeOfFreedom: Int): DoubleMatrix
	
	abstract fun calculateLocalLoadVector(): DoubleMatrix
	
	abstract fun getDirections(): IntArray
	
	abstract fun getDirection(index: Int): Int
	
	fun calculateGlobalLoadVector(degreeOfFreedom: Int): DoubleMatrix {
		val B = calculateIncidenceMatrix(degreeOfFreedom)
		val f = calculateLocalLoadVector()
		val P = B.transpose() * f
		return P
	}
	
	override fun hashCode(): Int {
		return mId
	}
	
	override fun equals(other: Any?): Boolean {
		return this.mId == (other as NodeAbs).mId
	}
}