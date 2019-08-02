package elems.planetruss

import elems.NodeAbs
import utils.DOF_NODE_PLANE_TRUSS
import utils.INVALID_DIRECTION
import utils.SupportCondition
import vsca.doublematrix.lib.DoubleMatrix

class NodePT(id: Int, x: Double, y: Double, suppCondStrings: Array<String>, loadValues: DoubleArray, stiffValues: DoubleArray) :
		NodeAbs(id, x, y, 0.0, suppCondStrings, loadValues, stiffValues) {
	
	override fun calculateIncidenceMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val matrixB = DoubleMatrix(DOF_NODE_PLANE_TRUSS, degreeOfFreedom)
		val e = getDirections()
		for (i in 1..DOF_NODE_PLANE_TRUSS)
			matrixB[i - 1, e[i - 1] - 1] = 1.0
		
		return matrixB
	}
	
	override fun calculateLocalLoadVector(): DoubleMatrix {
		val f = DoubleMatrix(DOF_NODE_PLANE_TRUSS, 1)
		mSupportCondition.forEachIndexed { index, supportCondition ->
			if (supportCondition == SupportCondition.FREE && mLoadValues[index] != 0.0) {
				f[index, 0] = mLoadValues[index]
			}
		}
		return f
	}
	
	override fun getDirections(): IntArray {
		return intArrayOf(
			DOF_NODE_PLANE_TRUSS * mId - 1,
			DOF_NODE_PLANE_TRUSS * mId
		)
	}
	
	override fun getDirection(index: Int): Int = when (index) {
		0 -> DOF_NODE_PLANE_TRUSS * mId - 1
		1 -> DOF_NODE_PLANE_TRUSS * mId
		else -> INVALID_DIRECTION
	}
}