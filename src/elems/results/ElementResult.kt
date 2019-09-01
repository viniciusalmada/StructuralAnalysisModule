package elems.results

import elems.Element
import matrixes.IncidenceVector
import matrixes.RotatesMatrix
import vsca.doublematrix.lib.DoubleMatrix

@Suppress("UNUSED_PARAMETER")
class ElementResult(element: Element, D: DoubleMatrix, dof: Int) {

	private val dg: DoubleMatrix // Displacements global
	private val d: DoubleMatrix // Displacements local
	private val f: DoubleMatrix // Forces local
	private val fg: DoubleMatrix // Forces global
	private val internalDisplacements: ArrayList<Double> = ArrayList()
	private val internalAxialForces: ArrayList<Double> = ArrayList()
	private val internalBendingMoment: ArrayList<Double> = ArrayList()
	private val internalNormalForce: ArrayList<Double> = ArrayList()

	val id: Int = element.mId
	val rg: DoubleMatrix // Support Reactions
	
	init {
		val matrixB = IncidenceVector(element).vector()
		val matrixR = RotatesMatrix(element).matrix()
		val k = element.calculateLocalStiffnessMatrixOnLocalSystem()
		val f = element.calculateLocalLoadVectorOnLocalSystem()
		
		this.dg = matrixB * D
		this.d = matrixR * this.dg
		this.f = (k * this.d) + f
		fg = matrixR.transpose() * this.f
		rg = matrixB.transpose() * (matrixR.transpose() * (this.f - f))
	}

	/*private fun calculateInternalDisplacements(){
		if (m)
	}*/
}
