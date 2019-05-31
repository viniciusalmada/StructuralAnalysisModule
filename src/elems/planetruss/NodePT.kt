package elems.planetruss

import elems.NodeAbs
import utils.DOF_NODE_PLANE_TRUSS
import utils.INVALID_DIRECTION
import utils.SupportCondition
import vsca.doublematrix.lib.DoubleMatrix

class NodePT(id: Int, x: Double, y: Double, suppCondStrings: Array<String>, mSupportValues: DoubleArray) :
	NodeAbs(id, x, y, 0.0, suppCondStrings, mSupportValues) {
	
	override fun calculateStiffnessMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val K = DoubleMatrix(degreeOfFreedom)
		mSupportCondition.forEachIndexed { index, supportCondition ->
			if (supportCondition == SupportCondition.SPRING) {
				val k = mSupportValues[index]
				val direction = when (index) {
					0 -> DOF_NODE_PLANE_TRUSS * mId - 1
					1 -> DOF_NODE_PLANE_TRUSS * mId
					else -> INVALID_DIRECTION
				}
				K[direction - 1, direction - 1] += k
			}
		}
		return K
	}
	
	override fun calculateIncidenceMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val B = DoubleMatrix(DOF_NODE_PLANE_TRUSS, degreeOfFreedom)
		val e = getDirections()
		for (i in 1..DOF_NODE_PLANE_TRUSS)
			B[i - 1, e[i - 1] - 1] = 1.0
		
		return B
	}
	
	override fun calculateLocalLoadVector(): DoubleMatrix {
		val f = DoubleMatrix(DOF_NODE_PLANE_TRUSS, 1)
		mSupportCondition.forEachIndexed { index, supportCondition ->
			if (supportCondition == SupportCondition.FREE && mSupportValues[index] != 0.0) {
				f[index, 0] = mSupportValues[index]
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
}