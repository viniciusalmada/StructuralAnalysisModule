package elems.planetruss

import elems.ElementAbs
import model.StructureModel
import utils.DOF_ELEM_PLANE_TRUSS
import utils.DOF_NODE_PLANE_TRUSS
import utils.StructureType
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
		val matrixB = DoubleMatrix(DOF_ELEM_PLANE_TRUSS, degreeOfFreedom)
		val i = this.mNode1.mId
		val j = this.mNode2.mId
		val e = intArrayOf(
			DOF_NODE_PLANE_TRUSS * i - 1,
			DOF_NODE_PLANE_TRUSS * i,
			DOF_NODE_PLANE_TRUSS * j - 1,
			DOF_NODE_PLANE_TRUSS * j
		)
		for (l in 1..DOF_ELEM_PLANE_TRUSS)
			matrixB[l - 1, e[l - 1] - 1] = 1.0
		return matrixB
	}
	
	override fun calculateLocalStiffnessMatrixOnLocalSystem(): DoubleMatrix {
		val k = DoubleMatrix(DOF_ELEM_PLANE_TRUSS)
		k[0, 0] = 1.0
		k[0, 2] = -1.0
		k[2, 0] = -1.0
		k[2, 2] = 1.0
		
		return k * (axialStiff() / length())
	}
	
	override fun calculateRotationMatrix(): DoubleMatrix {
		val matrixR = DoubleMatrix(DOF_ELEM_PLANE_TRUSS)
		matrixR[0, 0] = cosA()
		matrixR[0, 1] = sinA()
		matrixR[1, 0] = -sinA()
		matrixR[1, 1] = cosA()
		matrixR[2, 2] = cosA()
		matrixR[2, 3] = sinA()
		matrixR[3, 2] = -sinA()
		matrixR[3, 3] = cosA()
		
		return matrixR
	}
	
	override fun calculateLocalLoadVectorOnLocalSystem(): DoubleMatrix {
		var r = DoubleMatrix(DOF_ELEM_PLANE_TRUSS, 1)
		if (this.mLoad == null)
			return r
		r = mLoad.getPlaneTrussSupportReaction(length(), sinA(), cosA())
		return r * -1.0
	}
	
	override fun getType(): StructureType = StructureType.PLANE_TRUSS
	
}