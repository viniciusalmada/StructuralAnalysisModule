package matrixes

import elems.Element
import elems.Node
import utils.*

class IncidenceVector private constructor(element: Element?, node: Node?) {
    private var mElement: Element? = null
    private var mNode: Node? = null
    private var i: Int = 0
    private var j: Int = 0
    private var idNode = 0

    constructor(element: Element) : this(element, null)

    constructor(node: Node) : this(null, node)

    init {
        if (element != null && node == null) {
            this.mElement = element
            this.mNode = null
            this.i = element.mNode1.mId
            this.j = element.mNode2.mId
        } else if (element == null && node != null) {
            this.mElement = null
            this.mNode = node
            this.idNode = node.mId
        }
    }

    fun vector(): IntArray {
        return if (mElement != null && mNode == null) {
            when (mElement!!.mStructureType) {
                StructureType.PLANE_FRAME -> planeFrameIncidenceElement()
                StructureType.BEAM -> beamIncidenceElement()
                StructureType.PLANE_TRUSS -> planeTrussIncidenceElement()
                StructureType.GRILLAGE -> grillageIncidenceElement()
            }
        } else {
            when (mNode!!.mStructureType) {
                StructureType.PLANE_FRAME -> planeFrameIncidenceNode()
                StructureType.BEAM -> beamIncidenceNode()
                StructureType.PLANE_TRUSS -> planeTrussIncidenceNode()
                StructureType.GRILLAGE -> grillageIncidenceNode()
            }
        }
    }

    private fun planeFrameIncidenceElement(): IntArray {
        val incidenceVector = IntArray(DOF_ELEM_PLANE_FRAME)
        incidenceVector[0] = DOF_NODE_PLANE_FRAME * i - 2
        incidenceVector[1] = DOF_NODE_PLANE_FRAME * i - 1
        incidenceVector[2] = DOF_NODE_PLANE_FRAME * i
        incidenceVector[3] = DOF_NODE_PLANE_FRAME * j - 2
        incidenceVector[4] = DOF_NODE_PLANE_FRAME * j - 1
        incidenceVector[5] = DOF_NODE_PLANE_FRAME * j
        return incidenceVector
    }

    private fun beamIncidenceElement(): IntArray {
        val incidenceVector = IntArray(DOF_ELEM_BEAM)
        incidenceVector[0] = DOF_NODE_BEAM * i - 1
        incidenceVector[1] = DOF_NODE_BEAM * i
        incidenceVector[2] = DOF_NODE_BEAM * j - 1
        incidenceVector[3] = DOF_NODE_BEAM * j
        return incidenceVector
    }

    private fun planeTrussIncidenceElement(): IntArray {
        val incidenceVector = IntArray(DOF_ELEM_PLANE_TRUSS)
        incidenceVector[0] = DOF_NODE_PLANE_TRUSS * i - 1
        incidenceVector[1] = DOF_NODE_PLANE_TRUSS * i
        incidenceVector[2] = DOF_NODE_PLANE_TRUSS * j - 1
        incidenceVector[3] = DOF_NODE_PLANE_TRUSS * j
        return incidenceVector
    }

    private fun grillageIncidenceElement(): IntArray {
        val incidenceVector = IntArray(DOF_ELEM_GRILLAGE)
        incidenceVector[0] = DOF_NODE_GRILLAGE * i - 2
        incidenceVector[1] = DOF_NODE_GRILLAGE * i - 1
        incidenceVector[2] = DOF_NODE_GRILLAGE * i
        incidenceVector[3] = DOF_NODE_GRILLAGE * j - 2
        incidenceVector[4] = DOF_NODE_GRILLAGE * j - 1
        incidenceVector[5] = DOF_NODE_GRILLAGE * j
        return incidenceVector
    }

    private fun planeFrameIncidenceNode(): IntArray {
        val incidenceVector = IntArray(DOF_NODE_PLANE_FRAME)
        incidenceVector[0] = DOF_NODE_PLANE_FRAME * idNode - 2
        incidenceVector[1] = DOF_NODE_PLANE_FRAME * idNode - 1
        incidenceVector[2] = DOF_NODE_PLANE_FRAME * idNode
        return incidenceVector
    }

    private fun beamIncidenceNode(): IntArray {
        val incidenceVector = IntArray(DOF_NODE_BEAM)
        incidenceVector[0] = DOF_NODE_BEAM * idNode - 1
        incidenceVector[1] = DOF_NODE_BEAM * idNode
        return incidenceVector
    }

    private fun planeTrussIncidenceNode(): IntArray {
        val incidenceVector = IntArray(DOF_NODE_PLANE_TRUSS)
        incidenceVector[0] = DOF_NODE_PLANE_TRUSS * idNode - 1
        incidenceVector[1] = DOF_NODE_PLANE_TRUSS * idNode
        return incidenceVector
    }

    private fun grillageIncidenceNode(): IntArray {
        val incidenceVector = IntArray(DOF_NODE_GRILLAGE)
        incidenceVector[0] = DOF_NODE_GRILLAGE * idNode - 2
        incidenceVector[1] = DOF_NODE_GRILLAGE * idNode - 1
        incidenceVector[2] = DOF_NODE_GRILLAGE * idNode
        return incidenceVector
    }

}