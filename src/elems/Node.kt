package elems

import matrixes.LoadsVector
import utils.StructureType
import utils.SupportCondition

class Node(
		val mId: Int,
		val mX: Double,
		val mY: Double,
		val mZ: Double,
		suppCondStrings: Array<String>,
		val mLoadValues: DoubleArray,
		val mStiffValues: DoubleArray,
		val mStructureType: StructureType
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

	fun localLoadsVector(): DoubleArray {
		return LoadsVector(this).vector()
	}
	
	override fun hashCode(): Int {
		return mId
	}
	
	override fun equals(other: Any?): Boolean {
		return this.mId == (other as Node).mId
	}
}