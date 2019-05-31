package elems

import utils.DOF_ELEM_PLANE_FRAME
import utils.DOF_ELEM_PLANE_TRUSS
import vsca.doublematrix.lib.DoubleMatrix
import java.lang.Math.pow

class DistributedLoad(
	val id: Int,
	val qxi: Double,
	val qxj: Double,
	val qyi: Double,
	val qyj: Double,
	val isLocalLoad: Boolean
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
			val q_xi = qyi * sinA + qxi * cosA
			val q_xj = qyj * sinA + qxj * cosA
			val q_yi = qyi * cosA - qxi * sinA
			val q_yj = qyj * cosA - qxj * sinA
			if (!hasHingeBegin && !hasHingeEnd) {
				return getOnFixedFixedPlaneFrame(L, q_xi, q_xj, q_yi, q_yj)
			} else if (hasHingeBegin && !hasHingeEnd) {
				return getOnHingeFixedPlaneFrame(L, q_xi, q_xj, q_yi, q_yj)
			} else if (!hasHingeBegin && hasHingeEnd) {
				return getOnFixedHingedPlaneFrame(L, q_xi, q_xj, q_yi, q_yj)
			} else if (hasHingeBegin && hasHingeEnd) {
				return getOnHingedHingedPlaneFrame(L, q_xi, q_xj, q_yi, q_yj)
			}
		}
		return DoubleMatrix(DOF_ELEM_PLANE_FRAME, 1)
	}
	
	fun getPlaneTrussSupportReaction(
		L: Double,
		sinA: Double,
		cosA: Double,
		hasHingeBegin: Boolean,
		hasHingeEnd: Boolean
	): DoubleMatrix {
		
		if (isLocalLoad) {
			return getOnHingedHingedPlaneTruss(L, qxi, qxj, qyi, qyj)
			
		} else {
			val q_xi = qyi * sinA + qxi * cosA
			val q_xj = qyj * sinA + qxj * cosA
			val q_yi = qyi * cosA - qxi * sinA
			val q_yj = qyj * cosA - qxj * sinA
			return getOnHingedHingedPlaneTruss(L, q_xi, q_xj, q_yi, q_yj)
			
		}
	}
}
