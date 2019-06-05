package elems.grillage

import elems.NodeAbs
import utils.DOF_NODE_GRILLAGE
import utils.INVALID_DIRECTION
import utils.SupportCondition
import vsca.doublematrix.lib.DoubleMatrix

class NodeG(id: Int, z: Double, x: Double, suppCondStrings: Array<String>, suppValues: DoubleArray) :
	NodeAbs(id, x, 0.0, z, suppCondStrings, suppValues) {
	
	
	override fun calculateStiffnessMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val K = DoubleMatrix(degreeOfFreedom)
		mSupportCondition.forEachIndexed { index, supportCondition ->
			val k = mSupportValues[index]
			val direction = when (index) {
				0 -> DOF_NODE_GRILLAGE * mId - 2
				1 -> DOF_NODE_GRILLAGE * mId - 1
				2 -> DOF_NODE_GRILLAGE * mId
				else -> INVALID_DIRECTION
			}
			K[direction - 1, direction - 1] += k
		}
		return K
	}
	
	override fun calculateIncidenceMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val B = DoubleMatrix(DOF_NODE_GRILLAGE, degreeOfFreedom)
		val e = getDirections()
		for (i in 1..B.rows)
			B[i - 1, e[i - 1] - 1] = 1.0
		
		return B
	}
	
	override fun calculateLocalLoadVector(): DoubleMatrix {
		val f = DoubleMatrix(DOF_NODE_GRILLAGE, 1)
		mSupportCondition.forEachIndexed { index, supportCondition ->
			if (supportCondition == SupportCondition.FREE && mSupportValues[index] != 0.0) {
				f[index, 0] = mSupportValues[index]
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
	
}