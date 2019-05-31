package utils

enum class SupportCondition {
	FREE, FIX, SPRING
}

const val ANALYSIS_PLANE_FRAME = "PLANE_FRAME"
const val ANALYSIS_PLANE_TRUSS = "PLANE_TRUSS"
const val DOF_NODE_PLANE_FRAME = 3
const val DOF_ELEM_PLANE_FRAME = 6
const val DOF_NODE_PLANE_TRUSS = 2
const val DOF_ELEM_PLANE_TRUSS = 4
const val NO_LOADS = -1
const val INVALID_DIRECTION = -1