package matrixes

import elems.Element
import utils.*
import vsca.doublematrix.lib.DoubleMatrix

class LoadsVector(private val element: Element) {
	private val mHasHingeBegin = element.mHasHingeBegin
	private val mHasHingeEnd = element.mHasHingeEnd
	private val cosine = element.cosA()
	private val sine = element.sinA()
	private val length = element.length()
	private val mLoad = element.mLoad
	
	fun vector(): DoubleMatrix {
		return when (element.getType()) {
			StructureType.PLANE_FRAME -> planeFrameLoads()
			StructureType.BEAM -> beamLoads()
			StructureType.PLANE_TRUSS -> planeTrussLoads()
			StructureType.GRILLAGE -> grillageLoads()
		}
	}
	
	private fun grillageLoads(): DoubleMatrix {
		var r = DoubleMatrix(DOF_ELEM_GRILLAGE, 1)
		if (mLoad == null)
			return r
		r = mLoad.getGrillageSupportReaction(length, mHasHingeBegin, mHasHingeEnd)
		return r * -1.0
	}
	
	private fun planeTrussLoads(): DoubleMatrix {
		var r = DoubleMatrix(DOF_ELEM_PLANE_TRUSS, 1)
		if (mLoad == null)
			return r
		r = mLoad.getPlaneTrussSupportReaction(length, sine, cosine)
		return r * -1.0
	}
	
	private fun beamLoads(): DoubleMatrix {
		var r = DoubleMatrix(DOF_ELEM_BEAM, 1)
		if (mLoad == null)
			return r
		r = mLoad.getBeamSupportReaction(length, mHasHingeBegin, mHasHingeEnd)
		return r * -1.0
	}
	
	private fun planeFrameLoads(): DoubleMatrix {
		var r = DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
		if (mLoad == null)
			return r
		r = mLoad.getPlaneFrameSupportReaction(length, sine, cosine, mHasHingeBegin, mHasHingeEnd)
		return r * -1.0
	}
	
}
