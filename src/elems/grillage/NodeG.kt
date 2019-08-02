package elems.grillage

import elems.NodeAbs
import utils.DOF_NODE_GRILLAGE
import utils.INVALID_DIRECTION
import utils.SupportCondition
import vsca.doublematrix.lib.DoubleMatrix

class NodeG(id: Int, z: Double, x: Double, suppCondStrings: Array<String>, loadValues: DoubleArray, stiffValues: DoubleArray) :
		NodeAbs(id, x, 0.0, z, suppCondStrings, loadValues, stiffValues) {
	
	
	override fun calculateIncidenceMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val matrixB = DoubleMatrix(DOF_NODE_GRILLAGE, degreeOfFreedom)
		val e = getDirections()
		for (i in 1..matrixB.rows)
			matrixB[i - 1, e[i - 1] - 1] = 1.0
		
		return matrixB
	}
	
	override fun calculateLocalLoadVector(): DoubleMatrix {
		val f = DoubleMatrix(DOF_NODE_GRILLAGE, 1)
		mSupportCondition.forEachIndexed { index, supportCondition ->
			if (supportCondition == SupportCondition.FREE && mLoadValues[index] != 0.0) {
				f[index, 0] = mLoadValues[index]
			}
		}
		return f
	}
	
	override fun getDirections(): IntArray {
		return intArrayOf(
			DOF_NODE_GRILLAGE * mId - 2,    //	Torsion Moment
			DOF_NODE_GRILLAGE * mId - 1,    //	Shear Force
			DOF_NODE_GRILLAGE * mId            //	Beam Moment
		)
	}
	
	override fun getDirection(index: Int): Int = when (index) {
		0 -> DOF_NODE_GRILLAGE * mId - 2
		1 -> DOF_NODE_GRILLAGE * mId - 1
		2 -> DOF_NODE_GRILLAGE * mId
		else -> INVALID_DIRECTION
	}
	
	
}