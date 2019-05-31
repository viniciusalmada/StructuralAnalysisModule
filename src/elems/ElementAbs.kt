package elems

import model.StructureModel
import vsca.doublematrix.lib.DoubleMatrix
import java.lang.Math.pow
import java.lang.Math.sqrt

@Suppress(
	"LocalVariableName", "UnnecessaryVariable", "FunctionName", "MemberVisibilityCanBePrivate",
	"ConvertSecondaryConstructorToPrimary"
)
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
	val mMaterial: Material = model.mMaterials.single { it.id == materialId }
	val mSection: Section = model.mSections.single { it.id == sectionId }
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
	protected fun L() = sqrt(pow(dx(), 2.0) + pow(dy(), 2.0))
	protected fun L2() = pow(L(), 2.0)
	protected fun L3() = pow(L(), 3.0)
	protected fun sinA() = dy() / L()
	protected fun cosA() = dx() / L()
	protected fun EI() = this.mMaterial.longitudinalElasticityModule * this.mSection.getInertiaMoment()
	protected fun EA() = this.mMaterial.longitudinalElasticityModule * this.mSection.getArea()
	
	override fun toString(): String {
		val element = StringBuilder()
		element.append("Elem$mId\t")
		element.append("L = ${L()} m\t")
		element.append("E = ${mMaterial.longitudinalElasticityModule} kN/m²\t")
		element.append("A = ${mSection.getArea()} m²\t")
		element.append("I = ${mSection.getInertiaMoment()} m4\t")
		return element.toString()
	}
	
	override fun hashCode(): Int {
		return mId
	}
	
	override fun equals(other: Any?): Boolean {
		return this.mId == (other as ElementAbs).mId
	}
}
