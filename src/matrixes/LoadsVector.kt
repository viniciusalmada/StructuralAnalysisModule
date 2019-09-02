package matrixes

import elems.Element
import elems.Node
import utils.*

class LoadsVector private constructor(element: Element?, node: Node?) {
    private var mElement: Element? = null
    private var mNode: Node? = null
    private var mHasHingeBegin = false
    private var mHasHingeEnd = false
    private var cosine = 0.0
    private var sine = 0.0
    private var length = 0.0
    private var mLoad = element?.mLoad

    constructor(element: Element) : this(element, null)

    constructor(node: Node) : this(null, node)

    init {
        if (element != null && node == null) {
            this.mElement = element
            this.mNode = null
            this.mHasHingeBegin = element.mHasHingeBegin
            this.mHasHingeEnd = element.mHasHingeEnd
            this.cosine = element.cosA()
            this.sine = element.sinA()
            this.length = element.length()
        } else if (element == null && node != null) {
            this.mElement = null
            this.mNode = node
        }
    }

    fun vector(): DoubleArray {
        return if (mElement != null && mNode == null) {
            when (mElement!!.mStructureType) {
                StructureType.PLANE_FRAME -> planeFrameLoadsElement()
                StructureType.BEAM -> beamLoadsElement()
                StructureType.PLANE_TRUSS -> planeTrussLoadsElement()
                StructureType.GRILLAGE -> grillageLoadsElement()
            }
        } else {
            when (mNode!!.mStructureType) {
                StructureType.PLANE_FRAME -> planeFrameLoadsNodes()
                StructureType.BEAM -> beamLoadsNodes()
                StructureType.PLANE_TRUSS -> planeTrussLoadsNodes()
                StructureType.GRILLAGE -> grillageLoadsNodes()
            }
        }
    }

    private fun planeFrameLoadsNodes(): DoubleArray {
        val f = DoubleArray(DOF_NODE_PLANE_FRAME)
        mNode!!.mSupportCondition.forEachIndexed { index, _ ->
            f[index] = mNode!!.mLoadValues[index]
        }
        return f
    }

    private fun beamLoadsNodes(): DoubleArray {
        val f = DoubleArray(DOF_NODE_BEAM)
        mNode!!.mSupportCondition.forEachIndexed { index, _ ->
            f[index] = mNode!!.mLoadValues[index]
        }
        return f
    }

    private fun planeTrussLoadsNodes(): DoubleArray {
        val f = DoubleArray(DOF_NODE_PLANE_TRUSS)
        mNode!!.mSupportCondition.forEachIndexed { index, _ ->
            f[index] = mNode!!.mLoadValues[index]
        }
        return f
    }

    private fun grillageLoadsNodes(): DoubleArray {
        val f = DoubleArray(DOF_NODE_GRILLAGE)
        mNode!!.mSupportCondition.forEachIndexed { index, _ ->
            f[index] = mNode!!.mLoadValues[index]
        }
        return f
    }

    private fun grillageLoadsElement(): DoubleArray {
        var r = DoubleArray(DOF_ELEM_GRILLAGE)
        if (mLoad == null)
            return r
        r = mLoad!!.getGrillageSupportReaction(length, mHasHingeBegin, mHasHingeEnd)
        return r * -1.0
    }

    private fun planeTrussLoadsElement(): DoubleArray {
        var r = DoubleArray(DOF_ELEM_PLANE_TRUSS)
        if (mLoad == null)
            return r
        r = mLoad!!.getPlaneTrussSupportReaction(length, sine, cosine)
        return r * -1.0
    }

    private fun beamLoadsElement(): DoubleArray {
        var r = DoubleArray(DOF_ELEM_BEAM)
        if (mLoad == null)
            return r
        r = mLoad!!.getBeamSupportReaction(length, mHasHingeBegin, mHasHingeEnd)
        return r * -1.0
    }

    private fun planeFrameLoadsElement(): DoubleArray {
        var r = DoubleArray(DOF_ELEM_PLANE_FRAME)
        if (mLoad == null)
            return r
        r = mLoad!!.getPlaneFrameSupportReaction(length, sine, cosine, mHasHingeBegin, mHasHingeEnd)
        return r * -1.0
    }

}
