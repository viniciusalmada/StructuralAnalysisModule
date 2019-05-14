package entitites

import vsca.doublematrix.lib.DoubleMatrix

@Suppress("UNUSED_PARAMETER")
class ElementResult(element: Element, D: DoubleMatrix, dof: Int) {
	val dg: DoubleMatrix // Displacements global
	val d: DoubleMatrix // Displacements local
	val f: DoubleMatrix // Forces local
	val fg: DoubleMatrix // Forces global
	val rg: DoubleMatrix // Support Reactions
	
	init {
		val I = element.calculateIncidenceMatrix(dof)
		val T = element.calculateRotationMatrix()
		val k_ = element.calculateLocalStiffnessMatrixOnLocalSystem()
		val f_ = element.calculateLocalLoadVectorOnLocalSystem()
		
		this.dg = element.calculateIncidenceMatrix(dof) * D
		this.d = element.calculateRotationMatrix() * this.dg
		this.f = (k_ * this.d) + f_
		fg = T.transpose() * f
		rg = I.transpose() * (T.transpose() * (f - f_))
	}
}
