package elems.planeframe

import elems.ElementAbs
import model.StructureModel
import utils.DOF_ELEM_PLANE_FRAME
import utils.DOF_NODE_PLANE_FRAME
import utils.StructureType
import vsca.doublematrix.lib.DoubleMatrix

class ElementPF(
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
		val matrixB = DoubleMatrix(DOF_ELEM_PLANE_FRAME, degreeOfFreedom)
		val i = this.mNode1.mId
		val j = this.mNode2.mId
		val e = intArrayOf(
				DOF_NODE_PLANE_FRAME * i - 2,
				DOF_NODE_PLANE_FRAME * i - 1,
				DOF_NODE_PLANE_FRAME * i,
				DOF_NODE_PLANE_FRAME * j - 2,
				DOF_NODE_PLANE_FRAME * j - 1,
				DOF_NODE_PLANE_FRAME * j
		)
		for (l in 1..DOF_ELEM_PLANE_FRAME)
			matrixB[l - 1, e[l - 1] - 1] = 1.0
		return matrixB
	}

	override fun calculateLocalStiffnessMatrixOnLocalSystem(): DoubleMatrix {
		var k = DoubleMatrix(DOF_ELEM_PLANE_FRAME)
		if (!mHasHingeBegin && !mHasHingeEnd) {
			k[0, 0] = axialStiff() / length()
			k[0, 3] = -axialStiff() / length()
			k[1, 1] = 12 * flexuralStiff() / cubicLength()
			k[1, 2] = 6 * flexuralStiff() / squareLength()
			k[1, 4] = -12 * flexuralStiff() / cubicLength()
			k[1, 5] = 6 * flexuralStiff() / squareLength()
			k[2, 1] = 6 * flexuralStiff() / squareLength()
			k[2, 2] = 4 * flexuralStiff() / length()
			k[2, 4] = -6 * flexuralStiff() / squareLength()
			k[2, 5] = 2 * flexuralStiff() / length()
			k[3, 0] = -axialStiff() / length()
			k[3, 3] = axialStiff() / length()
			k[4, 1] = -12 * flexuralStiff() / cubicLength()
			k[4, 2] = -6 * flexuralStiff() / squareLength()
			k[4, 4] = 12 * flexuralStiff() / cubicLength()
			k[4, 5] = -6 * flexuralStiff() / squareLength()
			k[5, 1] = 6 * flexuralStiff() / squareLength()
			k[5, 2] = 2 * flexuralStiff() / length()
			k[5, 4] = -6 * flexuralStiff() / squareLength()
			k[5, 5] = 4 * flexuralStiff() / length()
		} else if (mHasHingeBegin && !mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_PLANE_FRAME)
			k[0, 0] = axialStiff() / length()
			k[0, 3] = -axialStiff() / length()
			k[1, 1] = 3 * flexuralStiff() / cubicLength()
			k[1, 4] = -3 * flexuralStiff() / cubicLength()
			k[1, 5] = 3 * flexuralStiff() / squareLength()
			k[3, 0] = -axialStiff() / length()
			k[3, 3] = axialStiff() / length()
			k[4, 1] = -3 * flexuralStiff() / cubicLength()
			k[4, 4] = 3 * flexuralStiff() / cubicLength()
			k[4, 5] = -3 * flexuralStiff() / squareLength()
			k[5, 1] = 3 * flexuralStiff() / squareLength()
			k[5, 4] = -3 * flexuralStiff() / squareLength()
			k[5, 5] = 3 * flexuralStiff() / length()
		} else if (!mHasHingeBegin && mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_PLANE_FRAME)
			k[0, 0] = axialStiff() / length()
			k[0, 3] = -axialStiff() / length()
			k[1, 1] = 3 * flexuralStiff() / cubicLength()
			k[1, 2] = 3 * flexuralStiff() / squareLength()
			k[1, 4] = -3 * flexuralStiff() / cubicLength()
			k[2, 1] = 3 * flexuralStiff() / squareLength()
			k[2, 2] = 3 * flexuralStiff() / length()
			k[2, 4] = -3 * flexuralStiff() / squareLength()
			k[3, 0] = -axialStiff() / length()
			k[3, 3] = axialStiff() / length()
			k[4, 1] = -3 * flexuralStiff() / cubicLength()
			k[4, 2] = -3 * flexuralStiff() / squareLength()
			k[4, 4] = 3 * flexuralStiff() / cubicLength()
		} else if (mHasHingeBegin && mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_PLANE_FRAME)
			k[0, 0] = axialStiff() / length()
			k[0, 3] = -axialStiff() / length()
			k[3, 0] = -axialStiff() / length()
			k[3, 3] = axialStiff() / length()
		}

		return k
	}

	override fun calculateRotationMatrix(): DoubleMatrix {
		val matrixR = DoubleMatrix(DOF_ELEM_PLANE_FRAME)
		matrixR[0, 0] = cosA()
		matrixR[0, 1] = sinA()
		matrixR[1, 0] = -sinA()
		matrixR[1, 1] = cosA()
		matrixR[2, 2] = 1.0
		matrixR[3, 3] = cosA()
		matrixR[3, 4] = sinA()
		matrixR[4, 3] = -sinA()
		matrixR[4, 4] = cosA()
		matrixR[5, 5] = 1.0

		return matrixR
	}

	override fun calculateLocalLoadVectorOnLocalSystem(): DoubleMatrix {
		var r = DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
		if (this.mLoad == null)
			return r
		r = mLoad.getPlaneFrameSupportReaction(length(), sinA(), cosA(), mHasHingeBegin, mHasHingeEnd)
		return r * -1.0
	}

	override fun getType(): StructureType = StructureType.PLANE_FRAME

}