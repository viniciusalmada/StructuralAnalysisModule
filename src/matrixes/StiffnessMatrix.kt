package matrixes

import elems.Element
import utils.*
import vsca.doublematrix.lib.DoubleMatrix

class StiffnessMatrix(private val element: Element) {
	private val mHasHingeBegin = element.mHasHingeBegin
	private val mHasHingeEnd = element.mHasHingeEnd
	private val flexuralStiff = element.flexuralStiff()
	private val cubicLength = element.cubicLength()
	private val squareLength = element.squareLength()
	private val length = element.length()
	private val axialStiff = element.axialStiff()
	private val torsionStiff = element.torsionStiff()
	
	fun matrix(): DoubleMatrix {
		return when (element.getType()) {
			StructureType.PLANE_FRAME -> planeFrameStiffness()
			StructureType.BEAM -> beamStiffness()
			StructureType.PLANE_TRUSS -> planeTrussStiffness()
			StructureType.GRILLAGE -> grillageStiffness()
		}
	}
	
	private fun grillageStiffness(): DoubleMatrix {
		var k = DoubleMatrix(DOF_ELEM_GRILLAGE)
		if (!mHasHingeBegin && !mHasHingeEnd) {
			k[0, 0] = torsionStiff / length
			k[0, 3] = -torsionStiff / length
			k[1, 1] = 12 * flexuralStiff / cubicLength
			k[1, 2] = 6 * flexuralStiff / squareLength
			k[1, 4] = -12 * flexuralStiff / cubicLength
			k[1, 5] = 6 * flexuralStiff / squareLength
			k[2, 1] = 6 * flexuralStiff / squareLength
			k[2, 2] = 4 * flexuralStiff / length
			k[2, 4] = -6 * flexuralStiff / squareLength
			k[2, 5] = 2 * flexuralStiff / length
			k[3, 0] = -torsionStiff / length
			k[3, 3] = torsionStiff / length
			k[4, 1] = -12 * flexuralStiff / cubicLength
			k[4, 2] = -6 * flexuralStiff / squareLength
			k[4, 4] = 12 * flexuralStiff / cubicLength
			k[4, 5] = -6 * flexuralStiff / squareLength
			k[5, 1] = 6 * flexuralStiff / squareLength
			k[5, 2] = 2 * flexuralStiff / length
			k[5, 4] = -6 * flexuralStiff / squareLength
			k[5, 5] = 4 * flexuralStiff / length
		} else if (mHasHingeBegin && !mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_GRILLAGE)
			k[1, 1] = 3 * flexuralStiff / cubicLength
			k[1, 4] = -3 * flexuralStiff / cubicLength
			k[1, 5] = 3 * flexuralStiff / squareLength
			k[4, 1] = -3 * flexuralStiff / cubicLength
			k[4, 4] = 3 * flexuralStiff / cubicLength
			k[4, 5] = -3 * flexuralStiff / squareLength
			k[5, 1] = 3 * flexuralStiff / squareLength
			k[5, 4] = -3 * flexuralStiff / squareLength
			k[5, 5] = 3 * flexuralStiff / length
		} else if (!mHasHingeBegin && mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_GRILLAGE)
			k[1, 1] = 3 * flexuralStiff / cubicLength
			k[1, 2] = 3 * flexuralStiff / squareLength
			k[1, 4] = -3 * flexuralStiff / cubicLength
			k[2, 1] = 3 * flexuralStiff / squareLength
			k[2, 2] = 3 * flexuralStiff / length
			k[2, 4] = -3 * flexuralStiff / squareLength
			k[4, 1] = -3 * flexuralStiff / cubicLength
			k[4, 2] = -3 * flexuralStiff / squareLength
			k[4, 4] = 3 * flexuralStiff / cubicLength
		} else if (mHasHingeBegin && mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_GRILLAGE)
		}
		
		return k
	}
	
	private fun planeTrussStiffness(): DoubleMatrix {
		val k = DoubleMatrix(DOF_ELEM_PLANE_TRUSS)
		k[0, 0] = 1.0
		k[0, 2] = -1.0
		k[2, 0] = -1.0
		k[2, 2] = 1.0
		return k * (axialStiff / length)
	}
	
	private fun planeFrameStiffness(): DoubleMatrix {
		var k = DoubleMatrix(DOF_ELEM_PLANE_FRAME)
		if (!mHasHingeBegin && !mHasHingeEnd) {
			k[0, 0] = axialStiff / length
			k[0, 3] = -axialStiff / length
			k[1, 1] = 12 * flexuralStiff / cubicLength
			k[1, 2] = 6 * flexuralStiff / squareLength
			k[1, 4] = -12 * flexuralStiff / cubicLength
			k[1, 5] = 6 * flexuralStiff / squareLength
			k[2, 1] = 6 * flexuralStiff / squareLength
			k[2, 2] = 4 * flexuralStiff / length
			k[2, 4] = -6 * flexuralStiff / squareLength
			k[2, 5] = 2 * flexuralStiff / length
			k[3, 0] = -axialStiff / length
			k[3, 3] = axialStiff / length
			k[4, 1] = -12 * flexuralStiff / cubicLength
			k[4, 2] = -6 * flexuralStiff / squareLength
			k[4, 4] = 12 * flexuralStiff / cubicLength
			k[4, 5] = -6 * flexuralStiff / squareLength
			k[5, 1] = 6 * flexuralStiff / squareLength
			k[5, 2] = 2 * flexuralStiff / length
			k[5, 4] = -6 * flexuralStiff / squareLength
			k[5, 5] = 4 * flexuralStiff / length
		} else if (mHasHingeBegin && !mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_PLANE_FRAME)
			k[0, 0] = axialStiff / length
			k[0, 3] = -axialStiff / length
			k[1, 1] = 3 * flexuralStiff / cubicLength
			k[1, 4] = -3 * flexuralStiff / cubicLength
			k[1, 5] = 3 * flexuralStiff / squareLength
			k[3, 0] = -axialStiff / length
			k[3, 3] = axialStiff / length
			k[4, 1] = -3 * flexuralStiff / cubicLength
			k[4, 4] = 3 * flexuralStiff / cubicLength
			k[4, 5] = -3 * flexuralStiff / squareLength
			k[5, 1] = 3 * flexuralStiff / squareLength
			k[5, 4] = -3 * flexuralStiff / squareLength
			k[5, 5] = 3 * flexuralStiff / length
		} else if (!mHasHingeBegin && mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_PLANE_FRAME)
			k[0, 0] = axialStiff / length
			k[0, 3] = -axialStiff / length
			k[1, 1] = 3 * flexuralStiff / cubicLength
			k[1, 2] = 3 * flexuralStiff / squareLength
			k[1, 4] = -3 * flexuralStiff / cubicLength
			k[2, 1] = 3 * flexuralStiff / squareLength
			k[2, 2] = 3 * flexuralStiff / length
			k[2, 4] = -3 * flexuralStiff / squareLength
			k[3, 0] = -axialStiff / length
			k[3, 3] = axialStiff / length
			k[4, 1] = -3 * flexuralStiff / cubicLength
			k[4, 2] = -3 * flexuralStiff / squareLength
			k[4, 4] = 3 * flexuralStiff / cubicLength
		} else if (mHasHingeBegin && mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_PLANE_FRAME)
			k[0, 0] = axialStiff / length
			k[0, 3] = -axialStiff / length
			k[3, 0] = -axialStiff / length
			k[3, 3] = axialStiff / length
		}
		
		return k
	}
	
	private fun beamStiffness(): DoubleMatrix {
		var k = DoubleMatrix(DOF_ELEM_BEAM)
		if (!mHasHingeBegin && !mHasHingeEnd) {
			k[0, 0] = 12 * flexuralStiff / cubicLength
			k[0, 1] = 6 * flexuralStiff / squareLength
			k[0, 2] = -12 * flexuralStiff / cubicLength
			k[0, 3] = 6 * flexuralStiff / squareLength
			k[1, 0] = 6 * flexuralStiff / squareLength
			k[1, 1] = 4 * flexuralStiff / length
			k[1, 2] = -6 * flexuralStiff / squareLength
			k[1, 3] = 2 * flexuralStiff / length
			k[2, 0] = -12 * flexuralStiff / cubicLength
			k[2, 1] = -6 * flexuralStiff / squareLength
			k[2, 2] = 12 * flexuralStiff / cubicLength
			k[2, 3] = -6 * flexuralStiff / squareLength
			k[3, 0] = 6 * flexuralStiff / squareLength
			k[3, 1] = 2 * flexuralStiff / length
			k[3, 2] = -6 * flexuralStiff / squareLength
			k[3, 3] = 4 * flexuralStiff / length
		} else if (mHasHingeBegin && !mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_BEAM)
			k[0, 0] = 3 * flexuralStiff / cubicLength
			k[0, 2] = -3 * flexuralStiff / cubicLength
			k[0, 3] = 3 * flexuralStiff / squareLength
			k[2, 0] = -3 * flexuralStiff / cubicLength
			k[2, 2] = 3 * flexuralStiff / cubicLength
			k[2, 3] = -3 * flexuralStiff / squareLength
			k[3, 0] = 3 * flexuralStiff / squareLength
			k[3, 2] = -3 * flexuralStiff / squareLength
			k[3, 3] = 3 * flexuralStiff / length
		} else if (!mHasHingeBegin && mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_BEAM)
			k[0, 0] = 3 * flexuralStiff / cubicLength
			k[0, 1] = 3 * flexuralStiff / squareLength
			k[0, 2] = -3 * flexuralStiff / cubicLength
			k[1, 0] = 3 * flexuralStiff / squareLength
			k[1, 1] = 3 * flexuralStiff / length
			k[1, 2] = -3 * flexuralStiff / squareLength
			k[2, 0] = -3 * flexuralStiff / cubicLength
			k[2, 1] = -3 * flexuralStiff / squareLength
			k[2, 2] = 3 * flexuralStiff / cubicLength
		} else if (mHasHingeBegin && mHasHingeEnd) {
			k = DoubleMatrix(DOF_ELEM_BEAM)
		}
		
		return k
	}
	
	
}