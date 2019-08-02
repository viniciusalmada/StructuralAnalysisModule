package elems.results

import elems.ElementAbs
import vsca.doublematrix.lib.DoubleMatrix

@Suppress("UNUSED_PARAMETER")
class ElementResult(element: ElementAbs, D: DoubleMatrix, dof: Int) {
	val id: Int = element.mId
	private val dg: DoubleMatrix // Displacements global
	private val d: DoubleMatrix // Displacements local
	private val f: DoubleMatrix // Forces local
	private val fg: DoubleMatrix // Forces global
	val rg: DoubleMatrix // Support Reactions
	
	init {
		val matrixB = element.calculateIncidenceMatrix(dof)
		val matrixR = element.calculateRotationMatrix()
		val k = element.calculateLocalStiffnessMatrixOnLocalSystem()
		val f = element.calculateLocalLoadVectorOnLocalSystem()
		
		this.dg = element.calculateIncidenceMatrix(dof) * D
		this.d = element.calculateRotationMatrix() * this.dg
		this.f = (k * this.d) + f
		fg = matrixR.transpose() * this.f
		rg = matrixB.transpose() * (matrixR.transpose() * (this.f - f))
	}
}
