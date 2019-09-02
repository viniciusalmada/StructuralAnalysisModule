package matrixes

import elems.Element
import utils.*
import vsca.doublematrix.lib.DoubleMatrix

class RotatesMatrix(private val element: Element) {
	private val cosine = element.cosA()
	private val sine = element.sinA()
	
	fun matrix(): DoubleMatrix {
		return when (element.mStructureType) {
			StructureType.PLANE_FRAME -> planeFrameRotationMatrix()
			StructureType.BEAM -> DoubleMatrix.eye(DOF_ELEM_BEAM)
			StructureType.PLANE_TRUSS -> planeTrussRotationMatrix()
			StructureType.GRILLAGE -> grillageRotationMatrix()
		}
	}
	
	private fun planeFrameRotationMatrix(): DoubleMatrix {
		val matrixR = DoubleMatrix(DOF_ELEM_PLANE_FRAME)
		matrixR[0, 0] = cosine
		matrixR[0, 1] = sine
		matrixR[1, 0] = -sine
		matrixR[1, 1] = cosine
		matrixR[2, 2] = 1.0
		matrixR[3, 3] = cosine
		matrixR[3, 4] = sine
		matrixR[4, 3] = -sine
		matrixR[4, 4] = cosine
		matrixR[5, 5] = 1.0
		return matrixR
	}
	
	private fun grillageRotationMatrix(): DoubleMatrix {
		val matrixR = DoubleMatrix(DOF_ELEM_GRILLAGE)
		matrixR[0, 0] = cosine
		matrixR[0, 2] = -sine
		matrixR[1, 1] = 1.0
		matrixR[2, 0] = sine
		matrixR[2, 2] = cosine
		matrixR[3, 3] = cosine
		matrixR[3, 5] = -sine
		matrixR[4, 4] = 1.0
		matrixR[5, 3] = sine
		matrixR[5, 5] = cosine
		
		return matrixR
	}
	
	private fun planeTrussRotationMatrix(): DoubleMatrix {
		val matrixR = DoubleMatrix(DOF_ELEM_PLANE_TRUSS)
		matrixR[0, 0] = cosine
		matrixR[0, 1] = sine
		matrixR[1, 0] = -sine
		matrixR[1, 1] = cosine
		matrixR[2, 2] = cosine
		matrixR[2, 3] = sine
		matrixR[3, 2] = -sine
		matrixR[3, 3] = cosine
		
		return matrixR
	}
	
}