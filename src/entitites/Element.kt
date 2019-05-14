package entitites

import model.StructureModel
import vsca.doublematrix.lib.DoubleMatrix
import java.lang.Math.pow
import java.lang.Math.sqrt

@Suppress(
	"LocalVariableName", "UnnecessaryVariable", "FunctionName", "MemberVisibilityCanBePrivate",
	"ConvertSecondaryConstructorToPrimary"
)
class Element {
	val id: Int
	val node_i: Node
	val node_j: Node
	val material: Material
	val section: Section
	val hasHingeBegin: Boolean
	val hasHingeEnd: Boolean
	var load: DistributedLoad?
	
	constructor(
		id: Int,
		nodeId_i: Int,
		nodeId_j: Int,
		materialId: Int,
		sectionId: Int,
		hasHingeBegin: Boolean = false,
		hasHingeEnd: Boolean = false,
		loadId: Int = -1,
		model: StructureModel
	) {
		this.id = id
		this.node_i = model.mNodes.single { it.id == nodeId_i }
		this.node_j = model.mNodes.single { it.id == nodeId_j }
		this.material = model.mMaterials.single { it.id == materialId }
		this.section = model.mSections.single { it.id == sectionId }
		this.hasHingeBegin = hasHingeBegin
		this.hasHingeEnd = hasHingeEnd
		try {
			this.load = model.mDistributedLoad.single { it.id == loadId }
		} catch (e: NoSuchElementException) {
			this.load = null
		}
	}
	/*private constructor(id: Int, node_i: Node, node_j: Node, material: Material, section: Section) : this(
		id,
		node_i,
		node_j,
		material,
		section,
		0.0,
		false
	)*/
	
	/*private constructor(
		id: Int,
		node_i: Node,
		node_j: Node,
		material: Material,
		section: Section,
		hasHingeBegin: Boolean,
		hasHingeEnd: Boolean
	) : this(
		id,
		node_i,
		node_j,
		material,
		section,
		0.0,
		false,
		hasHingeBegin, hasHingeEnd
	)*/
	
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
	
	/*private*/ fun calculateLocalStiffnessMatrixOnGlobalSystem(): DoubleMatrix {
		val k_ = calculateLocalStiffnessMatrixOnLocalSystem()
		val R = calculateRotationMatrix()
		val k = R.transpose() * k_ * R
		return k
	}
	
	/*private*/ fun calculateIncidenceMatrix(degreeOfFreedom: Int): DoubleMatrix {
		val B = DoubleMatrix(6, degreeOfFreedom)
		val i = this.node_i.id
		val j = this.node_j.id
		val e = intArrayOf(3 * i - 2, 3 * i - 1, 3 * i, 3 * j - 2, 3 * j - 1, 3 * j)
		for (l in 1..6)
			B[l - 1, e[l - 1] - 1] = 1.0
		return B
	}
	
	/*private*/ fun calculateLocalStiffnessMatrixOnLocalSystem(): DoubleMatrix {
		var k_ = DoubleMatrix(6, 6)
		k_[0, 0] = EA() / L()
		k_[0, 3] = -EA() / L()
		k_[1, 1] = 12 * EI() / L3()
		k_[1, 2] = 6 * EI() / L2()
		k_[1, 4] = -12 * EI() / L3()
		k_[1, 5] = 6 * EI() / L2()
		k_[2, 1] = 6 * EI() / L2()
		k_[2, 2] = 4 * EI() / L()
		k_[2, 4] = -6 * EI() / L2()
		k_[2, 5] = 2 * EI() / L()
		k_[3, 0] = -EA() / L()
		k_[3, 3] = EA() / L()
		k_[4, 1] = -12 * EI() / L3()
		k_[4, 2] = -6 * EI() / L2()
		k_[4, 4] = 12 * EI() / L3()
		k_[4, 5] = -6 * EI() / L2()
		k_[5, 1] = 6 * EI() / L2()
		k_[5, 2] = 2 * EI() / L()
		k_[5, 4] = -6 * EI() / L2()
		k_[5, 5] = 4 * EI() / L()
		
		if (hasHingeBegin && !hasHingeEnd) {
			k_ = DoubleMatrix(6, 6)
			k_[0, 0] = EA() / L()
			k_[0, 3] = -EA() / L()
			k_[1, 1] = 3 * EI() / L3()
			k_[1, 4] = -3 * EI() / L3()
			k_[1, 5] = 3 * EI() / L2()
			k_[3, 0] = -EA() / L()
			k_[3, 3] = EA() / L()
			k_[4, 1] = -3 * EI() / L3()
			k_[4, 4] = 3 * EI() / L3()
			k_[4, 5] = -3 * EI() / L2()
			k_[5, 1] = 3 * EI() / L2()
			k_[5, 4] = -3 * EI() / L2()
			k_[5, 5] = 3 * EI() / L()
		} else if (!hasHingeBegin && hasHingeEnd) {
			k_ = DoubleMatrix(6, 6)
			k_[0, 0] = EA() / L()
			k_[0, 3] = -EA() / L()
			k_[1, 1] = 3 * EI() / L3()
			k_[1, 2] = 3 * EI() / L2()
			k_[1, 4] = -3 * EI() / L3()
			k_[2, 1] = 3 * EI() / L2()
			k_[2, 2] = 3 * EI() / L()
			k_[2, 4] = -3 * EI() / L2()
			k_[3, 0] = -EA() / L()
			k_[3, 3] = EA() / L()
			k_[4, 1] = -3 * EI() / L3()
			k_[4, 2] = -3 * EI() / L2()
			k_[4, 4] = 3 * EI() / L3()
		} else if (hasHingeBegin && hasHingeEnd) {
			k_ = DoubleMatrix(6, 6)
			k_[0, 0] = EA() / L()
			k_[0, 3] = -EA() / L()
			k_[3, 0] = -EA() / L()
			k_[3, 3] = EA() / L()
		}
		
		return k_
	}
	
	/*private*/ fun calculateRotationMatrix(): DoubleMatrix {
		val R = DoubleMatrix(6, 6)
		R[0, 0] = cosA()
		R[0, 1] = sinA()
		R[1, 0] = -sinA()
		R[1, 1] = cosA()
		R[2, 2] = 1.0
		R[3, 3] = cosA()
		R[3, 4] = sinA()
		R[4, 3] = -sinA()
		R[4, 4] = cosA()
		R[5, 5] = 1.0
		
		return R
	}
	
	private fun calculateLocalLoadVectorOnGlobalSystem(): DoubleMatrix {
		val f_ = calculateLocalLoadVectorOnLocalSystem()
		val R = calculateRotationMatrix()
		val f = R.transpose() * f_
		return f
	}
	
	/*private*/ fun calculateLocalLoadVectorOnLocalSystem(): DoubleMatrix {
		var r = DoubleMatrix(6, 1)
		if (this.load == null)
			return r
		r = load!!.getSupportReaction(L(), sinA(), cosA(), hasHingeBegin, hasHingeEnd)
		return r * -1.0
	}
	
	private fun dx() = this.node_j.x - this.node_i.x
	private fun dy() = this.node_j.y - this.node_i.y
	private fun L() = sqrt(pow(dx(), 2.0) + pow(dy(), 2.0))
	private fun L2() = pow(L(), 2.0)
	private fun L3() = pow(L(), 3.0)
	private fun sinA() = dy() / L()
	private fun cosA() = dx() / L()
	private fun EI() = this.material.longitudinalElasticityModule * this.section.getInertiaMoment()
	private fun EA() = this.material.longitudinalElasticityModule * this.section.getArea()
	
	override fun toString(): String {
		val element = StringBuilder()
		element.append("Elem$id\t")
		element.append("L = ${L()} m\t")
		element.append("E = ${material.longitudinalElasticityModule} kN/m²\t")
		element.append("A = ${section.getArea()} m²\t")
		element.append("I = ${section.getInertiaMoment()} m4\t")
		return element.toString()
	}
	
	override fun hashCode(): Int {
		return id
	}
	
	override fun equals(other: Any?): Boolean {
		return this.id == (other as Element).id
	}
}
