package elems.beam

import elems.NodeAbs
import utils.DOF_NODE_BEAM
import utils.INVALID_DIRECTION
import utils.SupportCondition
import vsca.doublematrix.lib.DoubleMatrix

class NodeB(id: Int, x: Double, suppCondStrings: Array<String>, loadValues: DoubleArray, stiffValues: DoubleArray) :
		NodeAbs(id, x, 0.0, 0.0, suppCondStrings, loadValues, stiffValues) {
	
	override fun calculateIncidenceMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val B = DoubleMatrix(DOF_NODE_BEAM, degreeOfFreedom)
		val e = getDirections()
		for (i in 1..B.rows)
			B[i - 1, e[i - 1] - 1] = 1.0
		
		return B
	}
	
	override fun calculateLocalLoadVector(): DoubleMatrix {
		val f = DoubleMatrix(DOF_NODE_BEAM, 1)
		mSupportCondition.forEachIndexed { index, supportCondition ->
			if (supportCondition == SupportCondition.FREE && mLoadValues[index] != 0.0) {
				f[index, 0] = mLoadValues[index]
			}
		}
		return f
	}
	
	override fun getDirections(): IntArray {
		return intArrayOf(
			DOF_NODE_BEAM * mId - 1,
			DOF_NODE_BEAM * mId
		)
	}
	
	override fun getDirection(index: Int): Int = when (index) {
		0 -> DOF_NODE_BEAM * mId - 1
		1 -> DOF_NODE_BEAM * mId
		else -> INVALID_DIRECTION
	}
}