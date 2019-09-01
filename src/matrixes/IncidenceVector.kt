package matrixes

import elems.Element
import utils.*
import vsca.doublematrix.lib.DoubleMatrix

class IncidenceVector(private val element: Element) {
	private val i = element.mNode1.mId
	private val j = element.mNode2.mId
	
	fun vector(): DoubleMatrix {
		return when (element.getType()) {
			StructureType.PLANE_FRAME -> planeFrameIncidence()
			StructureType.BEAM -> beamIncidence()
			StructureType.PLANE_TRUSS -> planeTrussIncidence()
			StructureType.GRILLAGE -> grillageIncidence()
		}
	}
	
	private fun grillageIncidence(): DoubleMatrix {
		val incidenceVector = DoubleMatrix(DOF_ELEM_GRILLAGE, 1)
		incidenceVector[0, 0] = DOF_NODE_GRILLAGE * i - 2
		incidenceVector[1, 0] = DOF_NODE_GRILLAGE * i - 1
		incidenceVector[2, 0] = DOF_NODE_GRILLAGE * i
		incidenceVector[3, 0] = DOF_NODE_GRILLAGE * j - 2
		incidenceVector[4, 0] = DOF_NODE_GRILLAGE * j - 1
		incidenceVector[5, 0] = DOF_NODE_GRILLAGE * j
		return incidenceVector
	}
	
	private fun planeTrussIncidence(): DoubleMatrix {
		val incidenceVector = DoubleMatrix(DOF_ELEM_PLANE_TRUSS, 1)
		incidenceVector[0, 0] = DOF_NODE_PLANE_TRUSS * i - 1
		incidenceVector[1, 0] = DOF_NODE_PLANE_TRUSS * i
		incidenceVector[2, 0] = DOF_NODE_PLANE_TRUSS * j - 1
		incidenceVector[3, 0] = DOF_NODE_PLANE_TRUSS * j
		return incidenceVector
	}
	
	private fun beamIncidence(): DoubleMatrix {
		val incidenceVector = DoubleMatrix(DOF_ELEM_BEAM, 1)
		incidenceVector[0, 0] = DOF_NODE_BEAM * i - 1
		incidenceVector[1, 0] = DOF_NODE_BEAM * i
		incidenceVector[2, 0] = DOF_NODE_BEAM * j - 1
		incidenceVector[3, 0] = DOF_NODE_BEAM * j
		return incidenceVector
	}
	
	private fun planeFrameIncidence(): DoubleMatrix {
		val incidenceVector = DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
		incidenceVector[0, 0] = DOF_NODE_PLANE_FRAME * i - 2
		incidenceVector[1, 0] = DOF_NODE_PLANE_FRAME * i - 1
		incidenceVector[2, 0] = DOF_NODE_PLANE_FRAME * i
		incidenceVector[3, 0] = DOF_NODE_PLANE_FRAME * j - 2
		incidenceVector[4, 0] = DOF_NODE_PLANE_FRAME * j - 1
		incidenceVector[5, 0] = DOF_NODE_PLANE_FRAME * j
		return incidenceVector
	}
	
	
}