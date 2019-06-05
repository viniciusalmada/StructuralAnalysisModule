package elems.planetruss

import elems.ElementAbs
import model.StructureModel
import utils.DOF_ELEM_PLANE_TRUSS
import utils.DOF_NODE_PLANE_TRUSS
import vsca.doublematrix.lib.DoubleMatrix

class ElementPT(
	id: Int,
	nodeId_i: Int,
	nodeId_j: Int,
	materialId: Int,
	sectionId: Int,
	loadId: Int,
	model: StructureModel
) : ElementAbs(id, nodeId_i, nodeId_j, materialId, sectionId, true, true, loadId, model) {
	
	override fun calculateIncidenceMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val B = DoubleMatrix(DOF_ELEM_PLANE_TRUSS, degreeOfFreedom)
		val i = this.mNode_i.mId
		val j = this.mNode_j.mId
		val e = intArrayOf(
			DOF_NODE_PLANE_TRUSS * i - 1,
			DOF_NODE_PLANE_TRUSS * i,
			DOF_NODE_PLANE_TRUSS * j - 1,
			DOF_NODE_PLANE_TRUSS * j
		)
		for (l in 1..DOF_ELEM_PLANE_TRUSS)
			B[l - 1, e[l - 1] - 1] = 1.0
		return B
	}
	
	override fun calculateLocalStiffnessMatrixOnLocalSystem(): DoubleMatrix {
		val k_ = DoubleMatrix(DOF_ELEM_PLANE_TRUSS)
		k_[0, 0] = 1.0
		k_[0, 2] = -1.0
		k_[2, 0] = -1.0
		k_[2, 2] = 1.0
		
		return k_ * (EA() / L())
	}
	
	override fun calculateRotationMatrix(): DoubleMatrix {
		val R = DoubleMatrix(DOF_ELEM_PLANE_TRUSS)
		R[0, 0] = cosA()
		R[0, 1] = sinA()
		R[1, 0] = -sinA()
		R[1, 1] = cosA()
		R[2, 2] = cosA()
		R[2, 3] = sinA()
		R[3, 2] = -sinA()
		R[3, 3] = cosA()
		
		return R
	}
	
	override fun calculateLocalLoadVectorOnLocalSystem(): DoubleMatrix {
		var r = DoubleMatrix(DOF_ELEM_PLANE_TRUSS, 1)
		if (this.mLoad == null)
			return r
		r = mLoad.getPlaneTrussSupportReaction(L(), sinA(), cosA())
		return r * -1.0
	}
	
}