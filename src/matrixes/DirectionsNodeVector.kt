package matrixes

import elems.Node
import utils.*

class DirectionsNodeVector(private val node: Node) {

    fun direction(index: Int): Int {
        return when (node.mStructureType) {
            StructureType.BEAM -> beamDirection(index)
            StructureType.PLANE_FRAME -> planeFrameDirection(index)
            StructureType.PLANE_TRUSS -> planeTrussDirection(index)
            StructureType.GRILLAGE -> grillageDirection(index)
        }
    }

    private fun grillageDirection(index: Int): Int = when (index) {
        0 -> DOF_NODE_GRILLAGE * node.mId - 2
        1 -> DOF_NODE_GRILLAGE * node.mId - 1
        2 -> DOF_NODE_GRILLAGE * node.mId
        else -> INVALID_DIRECTION
    }

    private fun planeTrussDirection(index: Int): Int = when (index) {
        0 -> DOF_NODE_PLANE_TRUSS * node.mId - 1
        1 -> DOF_NODE_PLANE_TRUSS * node.mId
        else -> INVALID_DIRECTION
    }

    private fun planeFrameDirection(index: Int): Int = when (index) {
        0 -> DOF_NODE_PLANE_FRAME * node.mId - 2
        1 -> DOF_NODE_PLANE_FRAME * node.mId - 1
        2 -> DOF_NODE_PLANE_FRAME * node.mId
        else -> INVALID_DIRECTION
    }

    private fun beamDirection(index: Int): Int = when (index) {
        0 -> DOF_NODE_BEAM * node.mId - 1
        1 -> DOF_NODE_BEAM * node.mId
        else -> INVALID_DIRECTION
    }
}