package elems.results

import elems.Element
import matrixes.IncidenceVector
import matrixes.LoadsVector
import matrixes.RotatesMatrix
import matrixes.StiffnessMatrix
import utils.minus
import utils.plus
import utils.subVectorFromIncidence
import utils.times

@Suppress("UNUSED_PARAMETER")
class ElementResult(element: Element, vectorD: DoubleArray) {

	private val dg: DoubleArray // Displacements global
	private val d: DoubleArray // Displacements local
	private val f: DoubleArray // Forces local
	private val fg: DoubleArray // Forces global
	private val internalDisplacements: ArrayList<Double> = ArrayList()
	private val internalAxialForces: ArrayList<Double> = ArrayList()
	private val internalBendingMoment: ArrayList<Double> = ArrayList()
	private val internalNormalForce: ArrayList<Double> = ArrayList()

	val id: Int = element.mId
	val rg: DoubleArray // Support Reactions
	
	init {
		val vectorI = IncidenceVector(element).vector()
		val localR = RotatesMatrix(element).matrix()
		val localK = StiffnessMatrix(element).matrix()
		val localF = LoadsVector(element).vector()

		this.dg = vectorD.subVectorFromIncidence(vectorI)
		this.d = localR * this.dg
		this.f = (localK * this.d) + localF
		this.fg = localR.transpose() * this.f
		this.rg = (localR.transpose() * (this.f - localF))
	}

	/*private fun calculateInternalDisplacements(){
		if (m)
	}*/
}
