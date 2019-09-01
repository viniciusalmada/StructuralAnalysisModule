package elems

import matrixes.IncidenceVector
import matrixes.LoadsVector
import matrixes.RotatesMatrix
import matrixes.StiffnessMatrix
import model.StructureModel
import utils.StructureType
import vsca.doublematrix.lib.DoubleMatrix
import kotlin.math.pow
import kotlin.math.sqrt


abstract class Element(
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
	
	init {
		if (loadId != -1)
			this.mLoad = model.mDistributedLoads.single { it.id == loadId }
		else
			this.mLoad = null
	}
	
	abstract fun getType(): StructureType
	
	fun calculateGlobalStiffnessMatrix(): DoubleMatrix {
		val k = calculateLocalStiffnessMatrixOnGlobalSystem()
		val matrixB = IncidenceVector(this).vector()
		return matrixB.transpose() * k * matrixB
	}
	
	fun calculateGlobalLoadVector(): DoubleMatrix {
		val f = calculateLocalLoadVectorOnGlobalSystem()
		val matrixB = IncidenceVector(this).vector()
		return matrixB.transpose() * f
	}
	
	private fun calculateLocalStiffnessMatrixOnGlobalSystem(): DoubleMatrix {
		val k = StiffnessMatrix(this).matrix()
		val matrixR = RotatesMatrix(this).matrix()
		return matrixR.transpose() * k * matrixR
	}
	
	private fun calculateLocalLoadVectorOnGlobalSystem(): DoubleMatrix {
		val f = LoadsVector(this).vector()
		val matrixR = RotatesMatrix(this).matrix()
		return matrixR.transpose() * f
	}

	private fun dx() = this.mNode2.mCoordX - this.mNode1.mCoordX

	private fun dy() = this.mNode2.mCoordY - this.mNode1.mCoordY

	private fun dz() = this.mNode2.mCoordZ - this.mNode1.mCoordZ
	
	fun length(): Double {
		return if (getType() == StructureType.GRILLAGE)
			sqrt(dx().pow(2.0) + dz().pow(2.0))
		else
			sqrt(dx().pow(2.0) + dy().pow(2.0))
	}
	
	fun squareLength() = length().pow(2.0)
	
	fun cubicLength() = length().pow(3.0)
	
	fun sinA(): Double {
		return if (getType() == StructureType.GRILLAGE)
			dz() / length()
		else
			dy() / length()
	}
	
	fun cosA(): Double {
		return if (getType() == StructureType.GRILLAGE)
			dx() / length()
		else
			dx() / length()
	}
	
	fun flexuralStiff() = this.mMaterial.mLongElasticityModulus * this.mSection.mInertiaMomentZ
	
	fun axialStiff() = this.mMaterial.mLongElasticityModulus * this.mSection.mArea
	
	fun torsionStiff() = this.mMaterial.mTransverseElasticityModulus * this.mSection.getPolarInertiaMoment()
	
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
		return this.mId == (other as Element).mId
	}
}
