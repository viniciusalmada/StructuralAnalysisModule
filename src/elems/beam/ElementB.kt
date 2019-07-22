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
		val B = DoubleMatrix(DOF_ELEM_BEAM, degreeOfFreedom)
		val i = this.mNode_i.mId
		val j = this.mNode_j.mId
		val e = intArrayOf(
			DOF_NODE_BEAM * i - 1,
			DOF_NODE_BEAM * i,
			DOF_NODE_BEAM * j - 1,
			DOF_NODE_BEAM * j
		)
		for (l in 1..DOF_ELEM_BEAM)
			B[l - 1, e[l - 1] - 1] = 1.0
		return B
	}
	
	override fun calculateLocalStiffnessMatrixOnLocalSystem(): DoubleMatrix {
		var k_ = DoubleMatrix(DOF_ELEM_BEAM)
		if (!mHasHingeBegin && !mHasHingeEnd){
			k_[0, 0] = 12 * EIz() / L3()
			k_[0, 1] = 6 * EIz() / L2()
			k_[0, 2] = -12 * EIz() / L3()
			k_[0, 3] = 6 * EIz() / L2()
			k_[1, 0] = 6 * EIz() / L2()
			k_[1, 1] = 4 * EIz() / L()
			k_[1, 2] = -6 * EIz() / L2()
			k_[1, 3] = 2 * EIz() / L()
			k_[2, 0] = -12 * EIz() / L3()
			k_[2, 1] = -6 * EIz() / L2()
			k_[2, 2] = 12 * EIz() / L3()
			k_[2, 3] = -6 * EIz() / L2()
			k_[3, 0] = 6 * EIz() / L2()
			k_[3, 1] = 2 * EIz() / L()
			k_[3, 2] = -6 * EIz() / L2()
			k_[3, 3] = 4 * EIz() / L()
		} else if (mHasHingeBegin && !mHasHingeEnd) {
			k_ = DoubleMatrix(DOF_ELEM_BEAM)
			k_[0, 0] = 3 * EIz() / L3()
			k_[0, 2] = -3 * EIz() / L3()
			k_[0, 3] = 3 * EIz() / L2()
			k_[2, 0] = -3 * EIz() / L3()
			k_[2, 2] = 3 * EIz() / L3()
			k_[2, 3] = -3 * EIz() / L2()
			k_[3, 0] = 3 * EIz() / L2()
			k_[3, 2] = -3 * EIz() / L2()
			k_[3, 3] = 3 * EIz() / L()
		} else if (!mHasHingeBegin && mHasHingeEnd) {
			k_ = DoubleMatrix(DOF_ELEM_BEAM)
			k_[0, 0] = 3 * EIz() / L3()
			k_[0, 1] = 3 * EIz() / L2()
			k_[0, 2] = -3 * EIz() / L3()
			k_[1, 0] = 3 * EIz() / L2()
			k_[1, 1] = 3 * EIz() / L()
			k_[1, 2] = -3 * EIz() / L2()
			k_[2, 0] = -3 * EIz() / L3()
			k_[2, 1] = -3 * EIz() / L2()
			k_[2, 2] = 3 * EIz() / L3()
		} else if (mHasHingeBegin && mHasHingeEnd) {
			k_ = DoubleMatrix(DOF_ELEM_BEAM)
		}
		
		return k_
	}
	
	override fun calculateRotationMatrix(): DoubleMatrix {
		return DoubleMatrix.eye(DOF_ELEM_BEAM)
	}
	
	override fun calculateLocalLoadVectorOnLocalSystem(): DoubleMatrix {
		var r = DoubleMatrix(DOF_ELEM_BEAM, 1)
		if (this.mLoad == null)
			return r
		
		r = mLoad.getBeamSupportReaction(L(), mHasHingeBegin, mHasHingeEnd)
		return r * -1.0
	}
	
	override fun getType(): StructureType = StructureType.BEAM
	
}