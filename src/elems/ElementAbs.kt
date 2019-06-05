package elems

import model.StructureModel
import vsca.doublematrix.lib.DoubleMatrix
import java.lang.Math.pow
import java.lang.Math.sqrt


abstract class ElementAbs(
	id: Int,
	nodeId_i: Int,
	nodeId_j: Int,
	materialId: Int,
	sectionId: Int,
	hasHingeBegin: Boolean,
	hasHingeEnd: Boolean,
	loadId: Int,
	model: StructureModel
) {
	val mId: Int = id
	val mNode_i: NodeAbs = model.mNodes.single { it.mId == nodeId_i }
	val mNode_j: NodeAbs = model.mNodes.single { it.mId == nodeId_j }
	val mMaterial: Material = model.mMaterials.single { it.mId == materialId }
	val mSection: Section = model.mSections.single { it.mId == sectionId }
	val mHasHingeBegin: Boolean = hasHingeBegin
	val mHasHingeEnd: Boolean = hasHingeEnd
	val mLoad: DistributedLoad?
	
	init {
		if (loadId != -1)
			this.mLoad = model.mDistributedLoads.single { it.id == loadId }
		else
			this.mLoad = null
	}
	
	abstract fun calculateIncidenceMatrix(degreeOfFreedom: Int): DoubleMatrix
	
	abstract fun calculateLocalStiffnessMatrixOnLocalSystem(): DoubleMatrix
	
	abstract fun calculateRotationMatrix(): DoubleMatrix
	
	abstract fun calculateLocalLoadVectorOnLocalSystem(): DoubleMatrix
	
	fun calculateGlobalStiffnessMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val k = calculateLocalStiffnessMatrixOnGlobalSystem()
		val B = calculateIncidenceMatrix(degreeOfFreedom)
		val K = B.transpose() * k * B
		return K
	}
	
	fun calculateGlobalLoadVector(degreeOfFreedom: Int): DoubleMatrix {
		val f = calculateLocalLoadVectorOnGlobalSystem()
		val B = calculateIncidenceMatrix(degreeOfFreedom)
		val P = B.transpose() * f
		return P
	}
	
	fun calculateLocalStiffnessMatrixOnGlobalSystem(): DoubleMatrix {
		val k_ = calculateLocalStiffnessMatrixOnLocalSystem()
		val R = calculateRotationMatrix()
		val k = R.transpose() * k_ * R
		return k
	}
	
	fun calculateLocalLoadVectorOnGlobalSystem(): DoubleMatrix {
		val f_ = calculateLocalLoadVectorOnLocalSystem()
		val R = calculateRotationMatrix()
		val f = R.transpose() * f_
		return f
	}
	
	protected fun dx() = this.mNode_j.mX - this.mNode_i.mX
	protected fun dy() = this.mNode_j.mY - this.mNode_i.mY
	protected fun dz() = this.mNode_j.mZ - this.mNode_i.mZ
	protected fun L(): Double {
		if (dz() == 0.0)
			return sqrt(pow(dx(), 2.0) + pow(dy(), 2.0))
		else if (dy() == 0.0)
			return sqrt(pow(dx(), 2.0) + pow(dz(), 2.0))
		return Double.NaN
	}
	
	protected fun L2() = pow(L(), 2.0)
	protected fun L3() = pow(L(), 3.0)
	protected fun sinA(): Double {
		if (dz() == 0.0)
			return dy() / L()
		else if (dy() == 0.0)
			return dx() / L()
		return Double.NaN
	}
	
	protected fun cosA(): Double {
		if (dz() == 0.0)
			return dx() / L()
		else if (dy() == 0.0)
			return dz() / L()
		return Double.NaN
	}
	
	protected fun EIz() = this.mMaterial.mLongElasticityModulus * this.mSection.mInertiaMomentZ
	protected fun EA() = this.mMaterial.mLongElasticityModulus * this.mSection.mArea
	protected fun GJ() = this.mMaterial.mTransvElasticityModulus * this.mSection.getPolarInertiaMoment()
	
	override fun toString(): String {
		val element = StringBuilder()
		element.append("Elem$mId\t")
		element.append("L = ${L()} m\t")
		element.append("E = ${mMaterial.mLongElasticityModulus} kN/m²\t")
		element.append("A = ${mSection.mArea} m²\t")
		element.append("I = ${mSection.mInertiaMomentY} m4\t")
		return element.toString()
	}
	
	override fun hashCode(): Int {
		return mId
	}
	
	override fun equals(other: Any?): Boolean {
		return this.mId == (other as ElementAbs).mId
	}
}
