package elems

import model.StructureModel
import utils.StructureType
import vsca.doublematrix.lib.DoubleMatrix
import java.lang.Math.pow
import java.lang.Math.sqrt


abstract class ElementAbs(
		id: Int,
		nodeId1: Int,
		nodeId2: Int,
		materialId: Int,
		sectionId: Int,
		hasHingeBegin: Boolean,
		hasHingeEnd: Boolean,
		loadId: Int,
		model: StructureModel
) {

	private val mMaterial: Material = model.mMaterials.single { it.mId == materialId }
	private val mSection: Section = model.mSections.single { it.mId == sectionId }

	val mId: Int = id
	val mNode1: NodeAbs = model.mNodes.single { it.mId == nodeId1 }
	val mNode2: NodeAbs = model.mNodes.single { it.mId == nodeId2 }
	val mHasHingeBegin: Boolean = hasHingeBegin
	val mHasHingeEnd: Boolean = hasHingeEnd
	val mLoad: DistributedLoad?
	
	abstract fun getType(): StructureType
	
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
		val matrixB = calculateIncidenceMatrix(degreeOfFreedom)
		return matrixB.transpose() * k * matrixB
	}
	
	fun calculateGlobalLoadVector(degreeOfFreedom: Int): DoubleMatrix {
		val f = calculateLocalLoadVectorOnGlobalSystem()
		val matrixB = calculateIncidenceMatrix(degreeOfFreedom)
		return matrixB.transpose() * f
	}
	
	private fun calculateLocalStiffnessMatrixOnGlobalSystem(): DoubleMatrix {
		val k = calculateLocalStiffnessMatrixOnLocalSystem()
		val matrixR = calculateRotationMatrix()
		return matrixR.transpose() * k * matrixR
	}
	
	private fun calculateLocalLoadVectorOnGlobalSystem(): DoubleMatrix {
		val f = calculateLocalLoadVectorOnLocalSystem()
		val matrixR = calculateRotationMatrix()
		return matrixR.transpose() * f
	}

	private fun dx() = this.mNode2.mCoordX - this.mNode1.mCoordX

	private fun dy() = this.mNode2.mCoordY - this.mNode1.mCoordY

	private fun dz() = this.mNode2.mCoordZ - this.mNode1.mCoordZ

	protected fun length(): Double {
		return if (getType() == StructureType.GRILLAGE)
			sqrt(pow(dx(), 2.0) + pow(dz(), 2.0))
		else
			sqrt(pow(dx(), 2.0) + pow(dy(), 2.0))
	}
	
	protected fun squareLength() = pow(length(), 2.0)

	protected fun cubicLength() = pow(length(), 3.0)

	protected fun sinA(): Double {
		return if (getType() == StructureType.GRILLAGE)
			dz() / length()
		else
			dy() / length()
	}
	
	protected fun cosA(): Double {
		return if (getType() == StructureType.GRILLAGE)
			dx() / length()
		else
			dx() / length()
	}
	
	protected fun flexuralStiff() = this.mMaterial.mLongElasticityModulus * this.mSection.mInertiaMomentZ

	protected fun axialStiff() = this.mMaterial.mLongElasticityModulus * this.mSection.mArea

	protected fun torsionStiff() = this.mMaterial.mTransvElasticityModulus * this.mSection.getPolarInertiaMoment()
	
	override fun toString(): String {
		val element = StringBuilder()
		element.append("Elem$mId\t")
		element.append("length = ${length()} m\t")
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
