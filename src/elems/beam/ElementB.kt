package elems.beam

import elems.ElementAbs
import model.StructureModel
import utils.DOF_ELEM_BEAM
import utils.DOF_NODE_BEAM
import utils.StructureType
import vsca.doublematrix.lib.DoubleMatrix

class ElementB(
	id: Int,
	nodeId_i: Int,
	nodeId_j: Int,
	materialId: Int,
	sectionId: Int,
	hasHingeBegin: Boolean,
	hasHingeEnd: Boolean,
	loadId: Int,
	model: StructureModel
) : ElementAbs(id, nodeId_i, nodeId_j, materialId, sectionId, hasHingeBegin, hasHingeEnd, loadId, model) {
	
	override fun calculateIncidenceMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val matrixB = DoubleMatrix(DOF_ELEM_BEAM, degreeOfFreedom)
		val i = this.mNodei.mId
		val j = this.mNodej.mId
		val e = intArrayOf(
			DOF_NODE_BEAM * i - 1,
			DOF_NODE_BEAM * i,
			DOF_NODE_BEAM * j - 1,
			DOF_NODE_BEAM * j
		)
		for (l in 1..DOF_ELEM_BEAM)
			matrixB[l - 1, e[l - 1] - 1] = 1.0
		return matrixB
	}
	
	override fun calculateLocalStiffnessMatrixOnLocalSystem(): DoubleMatrix {
		var k = DoubleMatrix(DOF_ELEM_BEAM)
		if (!mHasHingeBegin && !mHasHingeEnd){
			k[0, 0] = 12 * flexuralStiff() / cubicLength()
			k[0, 1] = 6 * flexuralStiff() / squareLength()
			k[0, 2] = -12 * flexuralStiff() / cubicLength()
			k[0, 3] = 6 * flexuralStiff() / squareLength()
			k[1, 0] = 6 * flexuralStiff() / squareLength()
			k[1, 1] = 4 * flexuralStiff() / length()
			k[1, 2] = -6 * flexuralStiff() / squareLength()
			k[1, 3] = 2 * flexuralStiff() / length()
			k[2, 0] = -12 * flexuralStiff() / cubicLength()
			k[2, 1] = -6 * flexuralStiff() / squareLength()
			k[2, 2] = 12 * flexuralStiff() / cubicLength()
			k[2, 3] = -6 * flexuralStiff() / squareLength()
			k[3, 0] = 6 * flexuralStiff() / squareLength()
			k[3, 1] = 2 * flexuralStiff() / length()
			k[3, 2] = -6 * flexuralStiff() / squareLength()
			k[3, 3] = 4 * flexuralStiff() / length()
		} else if (mHasHingeBegin && !mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_BEAM)
			k[0, 0] = 3 * flexuralStiff() / cubicLength()
			k[0, 2] = -3 * flexuralStiff() / cubicLength()
			k[0, 3] = 3 * flexuralStiff() / squareLength()
			k[2, 0] = -3 * flexuralStiff() / cubicLength()
			k[2, 2] = 3 * flexuralStiff() / cubicLength()
			k[2, 3] = -3 * flexuralStiff() / squareLength()
			k[3, 0] = 3 * flexuralStiff() / squareLength()
			k[3, 2] = -3 * flexuralStiff() / squareLength()
			k[3, 3] = 3 * flexuralStiff() / length()
		} else if (!mHasHingeBegin && mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_BEAM)
			k[0, 0] = 3 * flexuralStiff() / cubicLength()
			k[0, 1] = 3 * flexuralStiff() / squareLength()
			k[0, 2] = -3 * flexuralStiff() / cubicLength()
			k[1, 0] = 3 * flexuralStiff() / squareLength()
			k[1, 1] = 3 * flexuralStiff() / length()
			k[1, 2] = -3 * flexuralStiff() / squareLength()
			k[2, 0] = -3 * flexuralStiff() / cubicLength()
			k[2, 1] = -3 * flexuralStiff() / squareLength()
			k[2, 2] = 3 * flexuralStiff() / cubicLength()
		} else if (mHasHingeBegin && mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_BEAM)
		}
		
		return k
	}
	
	override fun calculateRotationMatrix(): DoubleMatrix {
		return DoubleMatrix.eye(DOF_ELEM_BEAM)
	}
	
	override fun calculateLocalLoadVectorOnLocalSystem(): DoubleMatrix {
		var r = DoubleMatrix(DOF_ELEM_BEAM, 1)
		if (this.mLoad == null)
			return r
		
		r = mLoad.getBeamSupportReaction(length(), mHasHingeBegin, mHasHingeEnd)
		return r * -1.0
	}
	
	override fun getType(): StructureType = StructureType.BEAM
	
}