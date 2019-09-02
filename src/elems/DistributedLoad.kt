package elems

import utils.DOF_ELEM_BEAM
import utils.DOF_ELEM_GRILLAGE
import utils.DOF_ELEM_PLANE_FRAME
import utils.DOF_ELEM_PLANE_TRUSS
import kotlin.math.pow

class DistributedLoad(
		val id: Int,
		private val qxi: Double,
		private val qxj: Double,
		private val qyi: Double,
		private val qyj: Double,
		private val isLocalLoad: Boolean
) {
	
	companion object {
		private fun getOnFixedFixedPlaneFrame(
			L: Double,
			q_xi: Double,
			q_xj: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleArray {
			val r = DoubleArray(DOF_ELEM_PLANE_FRAME)
			r[0] = -(2 * q_xi + q_xj) * L / 6
			r[1] = -(7 * q_yi + 3 * q_yj) * L / 20
			r[2] = -(3 * q_yi + 2 * q_yj) * L.pow(2.0) / 60
			r[3] = -(q_xi + 2 * q_xj) * L / 6
			r[4] = -(3 * q_yi + 7 * q_yj) * L / 20
			r[5] = (2 * q_yi + 3 * q_yj) * L.pow(2.0) / 60
			return r
		}
		
		private fun getOnHingeFixedPlaneFrame(
			L: Double,
			q_xi: Double,
			q_xj: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleArray {
			val r = DoubleArray(DOF_ELEM_PLANE_FRAME)
			r[0] = -(2 * q_xi + q_xj) * L / 6
			r[1] = -(11 * q_yi + 4 * q_yj) * L / 40
			r[2] = 0.0
			r[3] = -(q_xi + 2 * q_xj) * L / 6
			r[4] = -(9 * q_yi + 16 * q_yj) * L / 40
			r[5] = (7 * q_yi + 8 * q_yj) * L.pow(2.0) / 120
			return r
		}
		
		private fun getOnFixedHingedPlaneFrame(
			L: Double,
			q_xi: Double,
			q_xj: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleArray {
			val r = DoubleArray(DOF_ELEM_PLANE_FRAME)
			r[0] = -(2 * q_xi + q_xj) * L / 6
			r[1] = -(16 * q_yi + 9 * q_yj) * L / 40
			r[2] = -(8 * q_yi + 7 * q_yj) * L.pow(2.0) / 120
			r[3] = -(q_xi + 2 * q_xj) * L / 6
			r[4] = -(4 * q_yi + 11 * q_yj) * L / 40
			r[5] = 0.0
			return r
		}
		
		private fun getOnHingedHingedPlaneFrame(
			L: Double,
			q_xi: Double,
			q_xj: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleArray {
			val r = DoubleArray(DOF_ELEM_PLANE_FRAME)
			r[0] = -(2 * q_xi + q_xj) * L / 6
			r[1] = -(2 * q_yi + q_yj) * L / 6
			r[2] = 0.0
			r[3] = -(q_xi + 2 * q_xj) * L / 6
			r[4] = -(q_yi + 2 * q_yj) * L / 6
			r[5] = 0.0
			return r
		}
		
		private fun getOnHingedHingedPlaneTruss(
			L: Double,
			q_xi: Double,
			q_xj: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleArray {
			val r = DoubleArray(DOF_ELEM_PLANE_TRUSS)
			r[0] = -(2 * q_xi + q_xj) * L / 6
			r[1] = -(2 * q_yi + q_yj) * L / 6
			r[2] = -(q_xi + 2 * q_xj) * L / 6
			r[3] = -(q_yi + 2 * q_yj) * L / 6
			return r
		}
		
		private fun getOnFixedFixedGrillage(
			L: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleArray {
			val r = DoubleArray(DOF_ELEM_GRILLAGE)
			r[1] = -(7 * q_yi + 3 * q_yj) * L / 20
			r[2] = -(3 * q_yi + 2 * q_yj) * L.pow(2.0) / 60
			r[4] = -(3 * q_yi + 7 * q_yj) * L / 20
			r[5] = (2 * q_yi + 3 * q_yj) * L.pow(2.0) / 60
			return r
		}
		
		private fun getOnHingeFixedGrillage(
			L: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleArray {
			val r = DoubleArray(DOF_ELEM_PLANE_FRAME)
			r[1] = -(11 * q_yi + 4 * q_yj) * L / 40
			r[2] = 0.0
			r[4] = -(9 * q_yi + 16 * q_yj) * L / 40
			r[5] = (7 * q_yi + 8 * q_yj) * L.pow(2.0) / 120
			return r
		}
		
		private fun getOnFixedHingedGrillage(
			L: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleArray {
			val r = DoubleArray(DOF_ELEM_PLANE_FRAME)
			r[1] = -(16 * q_yi + 9 * q_yj) * L / 40
			r[2] = -(8 * q_yi + 7 * q_yj) * L.pow(2.0) / 120
			r[4] = -(4 * q_yi + 11 * q_yj) * L / 40
			r[5] = 0.0
			return r
		}
		
		private fun getOnHingedHingedGrillage(
			L: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleArray {
			val r = DoubleArray(DOF_ELEM_PLANE_FRAME)
			r[1] = -(2 * q_yi + q_yj) * L / 6
			r[2] = 0.0
			r[4] = -(q_yi + 2 * q_yj) * L / 6
			r[5] = 0.0
			return r
		}

		private fun getOnFixedFixedBeam(L: Double, q_yi: Double, q_yj: Double): DoubleArray {
			val r = DoubleArray(DOF_ELEM_BEAM)
			r[0] = -(7 * q_yi + 3 * q_yj) * L / 20
			r[1] = -(3 * q_yi + 2 * q_yj) * L.pow(2.0) / 60
			r[2] = -(3 * q_yi + 7 * q_yj) * L / 20
			r[3] = (2 * q_yi + 3 * q_yj) * L.pow(2.0) / 60
			return r
		}

		private fun getOnHingeFixedBeam(L: Double, q_yi: Double, q_yj: Double): DoubleArray {
			val r = DoubleArray(DOF_ELEM_BEAM)
			r[0] = -(11 * q_yi + 4 * q_yj) * L / 40
			r[1] = 0.0
			r[2] = -(9 * q_yi + 16 * q_yj) * L / 40
			r[3] = (7 * q_yi + 8 * q_yj) * L.pow(2.0) / 120
			return r
		}

		private fun getOnFixedHingedBeam(L: Double, q_yi: Double, q_yj: Double): DoubleArray {
			val r = DoubleArray(DOF_ELEM_BEAM)
			r[0] = -(16 * q_yi + 9 * q_yj) * L / 40
			r[1] = -(8 * q_yi + 7 * q_yj) * L.pow(2.0) / 120
			r[2] = -(4 * q_yi + 11 * q_yj) * L / 40
			r[3] = 0.0
			return r
		}
	}
	
	fun getPlaneFrameSupportReaction(
		L: Double,
		sinA: Double,
		cosA: Double,
		hasHingeBegin: Boolean,
		hasHingeEnd: Boolean
	): DoubleArray {
		
		if (isLocalLoad) {
			if (!hasHingeBegin && !hasHingeEnd) {
				return getOnFixedFixedPlaneFrame(L, qxi, qxj, qyi, qyj)
			} else if (hasHingeBegin && !hasHingeEnd) {
				return getOnHingeFixedPlaneFrame(L, qxi, qxj, qyi, qyj)
			} else if (!hasHingeBegin && hasHingeEnd) {
				return getOnFixedHingedPlaneFrame(L, qxi, qxj, qyi, qyj)
			} else if (hasHingeBegin && hasHingeEnd) {
				return getOnHingedHingedPlaneFrame(L, qxi, qxj, qyi, qyj)
			}
		} else {
			val loadXi = qyi * sinA + qxi * cosA
			val loadXj = qyj * sinA + qxj * cosA
			val loadYi = qyi * cosA - qxi * sinA
			val loadYj = qyj * cosA - qxj * sinA
			if (!hasHingeBegin && !hasHingeEnd) {
				return getOnFixedFixedPlaneFrame(L, loadXi, loadXj, loadYi, loadYj)
			} else if (hasHingeBegin && !hasHingeEnd) {
				return getOnHingeFixedPlaneFrame(L, loadXi, loadXj, loadYi, loadYj)
			} else if (!hasHingeBegin && hasHingeEnd) {
				return getOnFixedHingedPlaneFrame(L, loadXi, loadXj, loadYi, loadYj)
			} else if (hasHingeBegin && hasHingeEnd) {
				return getOnHingedHingedPlaneFrame(L, loadXi, loadXj, loadYi, loadYj)
			}
		}
		return DoubleArray(DOF_ELEM_PLANE_FRAME)
	}
	
	fun getPlaneTrussSupportReaction(
		L: Double,
		sinA: Double,
		cosA: Double
	): DoubleArray {

		return if (isLocalLoad) {
			getOnHingedHingedPlaneTruss(L, qxi, qxj, qyi, qyj)
		} else {
			val loadXi = qyi * sinA + qxi * cosA
			val loadXj = qyj * sinA + qxj * cosA
			val loadYi = qyi * cosA - qxi * sinA
			val loadYj = qyj * cosA - qxj * sinA
			getOnHingedHingedPlaneTruss(L, loadXi, loadXj, loadYi, loadYj)
		}
	}
	
	fun getGrillageSupportReaction(
		L: Double,
		hasHingeBegin: Boolean,
		hasHingeEnd: Boolean
	): DoubleArray {
		if (qxi != 0.0 || qxj != 0.0) {
			throw RuntimeException("Grillage structure do not support loads on X direction")
		}
		if (!hasHingeBegin && !hasHingeEnd)
			return getOnFixedFixedGrillage(L, qyi, qyj)
		else if (hasHingeBegin && !hasHingeEnd)
			return getOnHingeFixedGrillage(L, qyi, qyj)
		else if (!hasHingeBegin && hasHingeEnd)
			return getOnFixedHingedGrillage(L, qyi, qyj)
		else if (!hasHingeBegin && !hasHingeEnd)
			return getOnHingedHingedGrillage(L, qyi, qyj)

		return DoubleArray(DOF_ELEM_GRILLAGE)
	}

	fun getBeamSupportReaction(L: Double, hasHingeBegin: Boolean, hasHingeEnd: Boolean): DoubleArray {
		if (qxi != 0.0 || qxj != 0.0) {
			throw RuntimeException("Beams structure do not support loads on X direction")
		}
		if (!hasHingeBegin && !hasHingeEnd) {
			return getOnFixedFixedBeam(L, qyi, qyj)
		} else if (hasHingeBegin && !hasHingeEnd) {
			return getOnHingeFixedBeam(L, qyi, qyj)
		} else if (!hasHingeBegin && hasHingeEnd) {
			return getOnFixedHingedBeam(L, qyi, qyj)
		}

		return DoubleArray(DOF_ELEM_BEAM)
	}
}
