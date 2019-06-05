package elems.grillage

import elems.ElementAbs
import model.StructureModel
import utils.DOF_ELEM_GRILLAGE
import utils.DOF_NODE_GRILLAGE
import vsca.doublematrix.lib.DoubleMatrix

class ElementG(
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
		val B = DoubleMatrix(DOF_ELEM_GRILLAGE, degreeOfFreedom)
		val i = this.mNode_i.mId
		val j = this.mNode_j.mId
		val e = intArrayOf(
			DOF_NODE_GRILLAGE * i - 2,
			DOF_NODE_GRILLAGE * i - 1,
			DOF_NODE_GRILLAGE * i,
			DOF_NODE_GRILLAGE * j - 2,
			DOF_NODE_GRILLAGE * j - 1,
			DOF_NODE_GRILLAGE * j
		)
		for (l in 1..DOF_ELEM_GRILLAGE)
			B[l - 1, e[l - 1] - 1] = 1.0
		return B
	}
	
	override fun calculateLocalStiffnessMatrixOnLocalSystem(): DoubleMatrix {
		var k_ = DoubleMatrix(DOF_ELEM_GRILLAGE)
		k_[0, 0] = GJ() / L()
		k_[0, 3] = -GJ() / L()
		
		k_[1, 1] = 12 * EIz() / L3()
		k_[1, 2] = 6 * EIz() / L2()
		k_[1, 4] = -12 * EIz() / L3()
		k_[1, 5] = 6 * EIz() / L2()
		
		k_[2, 1] = 6 * EIz() / L2()
		k_[2, 2] = 4 * EIz() / L()
		k_[2, 4] = -6 * EIz() / L2()
		k_[2, 5] = 2 * EIz() / L()
		
		k_[3, 0] = -GJ() / L()
		k_[3, 3] = GJ() / L()
		
		k_[4, 1] = -12 * EIz() / L3()
		k_[4, 2] = -6 * EIz() / L2()
		k_[4, 4] = 12 * EIz() / L3()
		k_[4, 5] = -6 * EIz() / L2()
		
		k_[5, 1] = 6 * EIz() / L2()
		k_[5, 2] = 2 * EIz() / L()
		k_[5, 4] = -6 * EIz() / L2()
		k_[5, 5] = 4 * EIz() / L()
		
		if (mHasHingeBegin && !mHasHingeEnd) {
			k_ = DoubleMatrix(DOF_ELEM_GRILLAGE)
			k_[0, 0] = GJ() / L()
			k_[0, 3] = -GJ() / L()
			k_[1, 1] = 3 * EIz() / L3()
			k_[1, 4] = -3 * EIz() / L3()
			k_[1, 5] = 3 * EIz() / L2()
			k_[3, 0] = -GJ() / L()
			k_[3, 3] = GJ() / L()
			k_[4, 1] = -3 * EIz() / L3()
			k_[4, 4] = 3 * EIz() / L3()
			k_[4, 5] = -3 * EIz() / L2()
			k_[5, 1] = 3 * EIz() / L2()
			k_[5, 4] = -3 * EIz() / L2()
			k_[5, 5] = 3 * EIz() / L()
		} else if (!mHasHingeBegin && mHasHingeEnd) {
			k_ = DoubleMatrix(DOF_ELEM_GRILLAGE)
			k_[0, 0] = GJ() / L()
			k_[0, 3] = -GJ() / L()
			k_[1, 1] = 3 * EIz() / L3()
			k_[1, 2] = 3 * EIz() / L2()
			k_[1, 4] = -3 * EIz() / L3()
			k_[2, 1] = 3 * EIz() / L2()
			k_[2, 2] = 3 * EIz() / L()
			k_[2, 4] = -3 * EIz() / L2()
			k_[3, 0] = -GJ() / L()
			k_[3, 3] = GJ() / L()
			k_[4, 1] = -3 * EIz() / L3()
			k_[4, 2] = -3 * EIz() / L2()
			k_[4, 4] = 3 * EIz() / L3()
		} else if (mHasHingeBegin && mHasHingeEnd) {
			k_ = DoubleMatrix(DOF_ELEM_GRILLAGE)
			k_[0, 0] = GJ() / L()
			k_[0, 3] = -GJ() / L()
			k_[3, 0] = -GJ() / L()
			k_[3, 3] = GJ() / L()
		}
		
		return k_
	}
	
	override fun calculateRotationMatrix(): DoubleMatrix {
		val R = DoubleMatrix(DOF_ELEM_GRILLAGE)
		R[0, 0] = cosA()
		R[0, 2] = sinA()
		R[1, 1] = 1.0
		R[2, 0] = -sinA()
		R[2, 2] = cosA()
		R[3, 3] = cosA()
		R[3, 5] = sinA()
		R[4, 4] = 1.0
		R[5, 3] = -sinA()
		R[5, 5] = cosA()
		
		return R
	}
	
	override fun calculateLocalLoadVectorOnLocalSystem(): DoubleMatrix {
		var r = DoubleMatrix(DOF_ELEM_GRILLAGE, 1)
		if (this.mLoad == null)
			return r
		r = mLoad.getGrillageSupportReaction(L(), mHasHingeBegin, mHasHingeEnd)
		return r * -1.0
	}
	
}