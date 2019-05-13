package entitites

import vsca.doublematrix.lib.DoubleMatrix
import java.lang.Math.pow

class DistributedLoad {
	
	val id: Int
	val qxi: Double
	val qxj: Double
	val qyi: Double
	val qyj: Double
	val isLocalLoad: Boolean
	
	companion object {
		private fun getOnFixedFixed(L: Double, q_xi: Double, q_xj: Double, q_yi: Double, q_yj: Double): DoubleMatrix {
			val r = DoubleMatrix(6, 1)
			r[0, 0] = -(2 * q_xi + q_xj) * L / 6
			r[1, 0] = -(7 * q_yi + 3 * q_yj) * L / 20
			r[2, 0] = -(3 * q_yi + 2 * q_yj) * pow(L, 2.0) / 60
			r[3, 0] = -(q_xi + 2 * q_xj) * L / 6
			r[4, 0] = -(3 * q_yi + 7 * q_yj) * L / 20
			r[5, 0] = (2 * q_yi + 3 * q_yj) * pow(L, 2.0) / 60
			return r
		}
		
		private fun getOnHingeFixed(L: Double, q_xi: Double, q_xj: Double, q_yi: Double, q_yj: Double): DoubleMatrix {
			val r = DoubleMatrix(6, 1)
			r[0, 0] = -(2 * q_xi + q_xj) * L / 6
			r[1, 0] = -(11 * q_yi + 4 * q_yj) * L / 40
			r[2, 0] = 0.0
			r[3, 0] = -(q_xi + 2 * q_xj) * L / 6
			r[4, 0] = -(9 * q_yi + 16 * q_yj) * L / 40
			r[5, 0] = (7 * q_yi + 8 * q_yj) * pow(L, 2.0) / 120
			return r
		}
		
		private fun getOnFixedHinged(L: Double, q_xi: Double, q_xj: Double, q_yi: Double, q_yj: Double): DoubleMatrix {
			val r = DoubleMatrix(6, 1)
			r[0, 0] = -(2 * q_xi + q_xj) * L / 6
			r[1, 0] = -(16 * q_yi + 9 * q_yj) * L / 40
			r[2, 0] = -(8 * q_yi + 7 * q_yj) * pow(L, 2.0) / 120
			r[3, 0] = -(q_xi + 2 * q_xj) * L / 6
			r[4, 0] = -(4 * q_yi + 11 * q_yj) * L / 40
			r[5, 0] = 0.0
			return r
		}
		
		private fun getOnHingedHinged(L: Double, q_xi: Double, q_xj: Double, q_yi: Double, q_yj: Double): DoubleMatrix {
			val r = DoubleMatrix(6, 1)
			r[0, 0] = -(2 * q_xi + q_xj) * L / 6
			r[1, 0] = -(2 * q_yi + q_yj) * L / 6
			r[2, 0] = 0.0
			r[3, 0] = -(q_xi + 2 * q_xj) * L / 6
			r[4, 0] = -(q_yi + 2 * q_yj) * L / 6
			r[5, 0] = 0.0
			return r
		}
	}
	
	constructor(id: Int, qx: Double, qy: Double, isLocalLoad: Boolean) {
		this.id = id
		this.qxi = qx
		this.qxj = qx
		this.qyi = qy
		this.qyj = qy
		this.isLocalLoad = isLocalLoad
	}
	
	constructor(id: Int, qxi: Double, qxj: Double, qyi: Double, qyj: Double, isLocalLoad: Boolean) {
		this.id = id
		this.qxi = qxi
		this.qxj = qxj
		this.qyi = qyi
		this.qyj = qyj
		this.isLocalLoad = isLocalLoad
	}
	
	fun getSupportReaction(
		L: Double,
		sinA: Double,
		cosA: Double,
		hasHingeBegin: Boolean,
		hasHingeEnd: Boolean
	): DoubleMatrix {
		
		if (isLocalLoad) {
			if (!hasHingeBegin && !hasHingeEnd) {
				return getOnFixedFixed(L, qxi, qxj, qyi, qyj)
			} else if (hasHingeBegin && !hasHingeEnd) {
				return getOnHingeFixed(L, qxi, qxj, qyi, qyj)
			} else if (!hasHingeBegin && hasHingeEnd) {
				return getOnFixedHinged(L, qxi, qxj, qyi, qyj)
			} else if (hasHingeBegin && hasHingeEnd) {
				return getOnHingedHinged(L, qxi, qxj, qyi, qyj)
			}
		} else {
			val q_xi = qyi * sinA + qxi * cosA
			val q_xj = qyj * sinA + qxj * cosA
			val q_yi = qyi * cosA - qxi * sinA
			val q_yj = qyj * cosA - qxj * sinA
			if (!hasHingeBegin && !hasHingeEnd) {
				return getOnFixedFixed(L, q_xi, q_xj, q_yi, q_yj)
			} else if (hasHingeBegin && !hasHingeEnd) {
				return getOnHingeFixed(L, q_xi, q_xj, q_yi, q_yj)
			} else if (!hasHingeBegin && hasHingeEnd) {
				return getOnFixedHinged(L, q_xi, q_xj, q_yi, q_yj)
			} else if (hasHingeBegin && hasHingeEnd) {
				return getOnHingedHinged(L, q_xi, q_xj, q_yi, q_yj)
			}
		}
		return DoubleMatrix(6, 1)
	}
}
