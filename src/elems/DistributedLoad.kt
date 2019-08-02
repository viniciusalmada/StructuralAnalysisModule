package elems

import utils.DOF_ELEM_BEAM
import utils.DOF_ELEM_GRILLAGE
import utils.DOF_ELEM_PLANE_FRAME
import utils.DOF_ELEM_PLANE_TRUSS
import vsca.doublematrix.lib.DoubleMatrix
import java.lang.Math.pow

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
		): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
			r[0, 0] = -(2 * q_xi + q_xj) * L / 6
			r[1, 0] = -(7 * q_yi + 3 * q_yj) * L / 20
			r[2, 0] = -(3 * q_yi + 2 * q_yj) * pow(L, 2.0) / 60
			r[3, 0] = -(q_xi + 2 * q_xj) * L / 6
			r[4, 0] = -(3 * q_yi + 7 * q_yj) * L / 20
			r[5, 0] = (2 * q_yi + 3 * q_yj) * pow(L, 2.0) / 60
			return r
		}
		
		private fun getOnHingeFixedPlaneFrame(
			L: Double,
			q_xi: Double,
			q_xj: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
			r[0, 0] = -(2 * q_xi + q_xj) * L / 6
			r[1, 0] = -(11 * q_yi + 4 * q_yj) * L / 40
			r[2, 0] = 0.0
			r[3, 0] = -(q_xi + 2 * q_xj) * L / 6
			r[4, 0] = -(9 * q_yi + 16 * q_yj) * L / 40
			r[5, 0] = (7 * q_yi + 8 * q_yj) * pow(L, 2.0) / 120
			return r
		}
		
		private fun getOnFixedHingedPlaneFrame(
			L: Double,
			q_xi: Double,
			q_xj: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
			r[0, 0] = -(2 * q_xi + q_xj) * L / 6
			r[1, 0] = -(16 * q_yi + 9 * q_yj) * L / 40
			r[2, 0] = -(8 * q_yi + 7 * q_yj) * pow(L, 2.0) / 120
			r[3, 0] = -(q_xi + 2 * q_xj) * L / 6
			r[4, 0] = -(4 * q_yi + 11 * q_yj) * L / 40
			r[5, 0] = 0.0
			return r
		}
		
		private fun getOnHingedHingedPlaneFrame(
			L: Double,
			q_xi: Double,
			q_xj: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
			r[0, 0] = -(2 * q_xi + q_xj) * L / 6
			r[1, 0] = -(2 * q_yi + q_yj) * L / 6
			r[2, 0] = 0.0
			r[3, 0] = -(q_xi + 2 * q_xj) * L / 6
			r[4, 0] = -(q_yi + 2 * q_yj) * L / 6
			r[5, 0] = 0.0
			return r
		}
		
		private fun getOnHingedHingedPlaneTruss(
			L: Double,
			q_xi: Double,
			q_xj: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_PLANE_TRUSS, 1)
			r[0, 0] = -(2 * q_xi + q_xj) * L / 6
			r[1, 0] = -(2 * q_yi + q_yj) * L / 6
			r[2, 0] = -(q_xi + 2 * q_xj) * L / 6
			r[3, 0] = -(q_yi + 2 * q_yj) * L / 6
			return r
		}
		
		private fun getOnFixedFixedGrillage(
			L: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_GRILLAGE, 1)
			r[1, 0] = -(7 * q_yi + 3 * q_yj) * L / 20
			r[2, 0] = -(3 * q_yi + 2 * q_yj) * pow(L, 2.0) / 60
			r[4, 0] = -(3 * q_yi + 7 * q_yj) * L / 20
			r[5, 0] = (2 * q_yi + 3 * q_yj) * pow(L, 2.0) / 60
			return r
		}
		
		private fun getOnHingeFixedGrillage(
			L: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
			r[1, 0] = -(11 * q_yi + 4 * q_yj) * L / 40
			r[2, 0] = 0.0
			r[4, 0] = -(9 * q_yi + 16 * q_yj) * L / 40
			r[5, 0] = (7 * q_yi + 8 * q_yj) * pow(L, 2.0) / 120
			return r
		}
		
		private fun getOnFixedHingedGrillage(
			L: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
			r[1, 0] = -(16 * q_yi + 9 * q_yj) * L / 40
			r[2, 0] = -(8 * q_yi + 7 * q_yj) * pow(L, 2.0) / 120
			r[4, 0] = -(4 * q_yi + 11 * q_yj) * L / 40
			r[5, 0] = 0.0
			return r
		}
		
		private fun getOnHingedHingedGrillage(
			L: Double,
			q_yi: Double,
			q_yj: Double
		): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
			r[1, 0] = -(2 * q_yi + q_yj) * L / 6
			r[2, 0] = 0.0
			r[4, 0] = -(q_yi + 2 * q_yj) * L / 6
			r[5, 0] = 0.0
			return r
		}
		
		private fun getOnFixedFixedBeam(L: Double, q_yi: Double, q_yj: Double): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_BEAM, 1)
			r[0, 0] = -(7 * q_yi + 3 * q_yj) * L / 20
			r[1, 0] = -(3 * q_yi + 2 * q_yj) * pow(L, 2.0) / 60
			r[2, 0] = -(3 * q_yi + 7 * q_yj) * L / 20
			r[3, 0] = (2 * q_yi + 3 * q_yj) * pow(L, 2.0) / 60
			return r
		}
		
		private fun getOnHingeFixedBeam(L: Double, q_yi: Double, q_yj: Double): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_BEAM, 1)
			r[0, 0] = -(11 * q_yi + 4 * q_yj) * L / 40
			r[1, 0] = 0.0
			r[2, 0] = -(9 * q_yi + 16 * q_yj) * L / 40
			r[3, 0] = (7 * q_yi + 8 * q_yj) * pow(L, 2.0) / 120
			return r
		}
		
		private fun getOnFixedHingedBeam(L: Double, q_yi: Double, q_yj: Double): DoubleMatrix {
			val r = DoubleMatrix(DOF_ELEM_BEAM, 1)
			r[0, 0] = -(16 * q_yi + 9 * q_yj) * L / 40
			r[1, 0] = -(8 * q_yi + 7 * q_yj) * pow(L, 2.0) / 120
			r[2, 0] = -(4 * q_yi + 11 * q_yj) * L / 40
			r[3, 0] = 0.0
			return r
		}
	}
	
	fun getPlaneFrameSupportReaction(
		L: Double,
		sinA: Double,
		cosA: Double,
		hasHingeBegin: Boolean,
		hasHingeEnd: Boolean
	): DoubleMatrix {
		
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
		return DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
	}
	
	fun getPlaneTrussSupportReaction(
		L: Double,
		sinA: Double,
		cosA: Double
	): DoubleMatrix {

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
	): DoubleMatrix {
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
		
		return DoubleMatrix(DOF_ELEM_GRILLAGE, 1)
	}
	
	fun getBeamSupportReaction(L: Double, hasHingeBegin: Boolean, hasHingeEnd: Boolean): DoubleMatrix {
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
		
		return DoubleMatrix(DOF_ELEM_BEAM, 1)
	}
}
