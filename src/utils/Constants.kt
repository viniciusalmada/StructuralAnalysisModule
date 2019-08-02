package utils

enum class SupportCondition {
	FREE, FIX, SPRING
}

enum class StructureType {
	BEAM, PLANE_FRAME, PLANE_TRUSS, GRILLAGE
}

const val ANALYSIS_PLANE_FRAME = "PLANE_FRAME"
const val ANALYSIS_BEAM = "BEAM"
const val ANALYSIS_PLANE_TRUSS = "PLANE_TRUSS"
const val ANALYSIS_GRILLAGE = "GRILLAGE"
const val DOF_NODE_BEAM = 2
const val DOF_ELEM_BEAM = 4
const val DOF_NODE_PLANE_FRAME = 3
const val DOF_ELEM_PLANE_FRAME = 6
const val DOF_NODE_PLANE_TRUSS = 2
const val DOF_ELEM_PLANE_TRUSS = 4
const val DOF_NODE_GRILLAGE = 3
const val DOF_ELEM_GRILLAGE = 6
const val INVALID_DIRECTION = -1