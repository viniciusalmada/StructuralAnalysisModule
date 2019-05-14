package analysis

import entitites.ElementResult
import entitites.NodeResult
import model.StructureModel
import utils.SupportCondition.FIX
import vsca.doublematrix.lib.DoubleMatrix
import vsca.doublematrix.lib.MatrixLib

class AnalysisFrame2D(private val model: StructureModel) {
	private var mDegreeOfFreedom: Int = 0
	
	fun doAnalysis(): Pair<ArrayList<ElementResult>, ArrayList<NodeResult>> {
		mDegreeOfFreedom = model.mNodes.size * 3
		val K = calculateStiffnessMatrix(true)
		val P = calculateLoadVector()
		val Kb = calculateStiffnessMatrixBoundaryCondition(K)
		val D = calculateDisplacementsVector(Kb, P)
		val R = calculateSupportReactionsVector(P, D)
		return calculateResults(D, R)
	}
	
	/**
	 * Calculate and assemble the global stiffness matrix of structure
	 *
	 * @return stiffness matrix
	 */
	private fun calculateStiffnessMatrix(includeSprings: Boolean): DoubleMatrix {
		var stiffMatrix = DoubleMatrix(mDegreeOfFreedom)
		model.mElements.forEach {
			val stiffMatrixOfElement = it.calculateGlobalStiffnessMatrix(mDegreeOfFreedom)
			stiffMatrix += stiffMatrixOfElement
		}
		if (includeSprings) {
			model.mNodes.forEach {
				val stiffMatrixOfNode = it.calculateStiffnessMatrix(mDegreeOfFreedom)
				stiffMatrix += stiffMatrixOfNode
			}
		}
		return stiffMatrix
	}
	
	/**
	 * Calculate and assemble the global loads vector of structure
	 *
	 * @return load vector
	 */
	private fun calculateLoadVector(): DoubleMatrix {
		var loadVector = DoubleMatrix(mDegreeOfFreedom, 1)
		model.mNodes.forEach {
			val loadVectorOfNode = it.calculateGlobalLoadVector(mDegreeOfFreedom)
			loadVector += loadVectorOfNode
		}
		model.mElements.forEach {
			val loadVectorOfElement = it.calculateGlobalLoadVector(mDegreeOfFreedom)
			loadVector += loadVectorOfElement
		}
		return loadVector
	}
	
	/**
	 * Assemble the stiffness matrix with boundary conditions
	 * from the support directions definitions
	 *
	 * @param K - global stiffness matrix (original)
	 * @return stiffness matrix with boundary conditions
	 */
	private fun calculateStiffnessMatrixBoundaryCondition(K: DoubleMatrix): DoubleMatrix {
		val Kb = K.copy()
		model.mNodes.forEach {
			it.supportCondition.forEachIndexed { i, supCond ->
				if (supCond == FIX) {
					/*
					 * index 0 -> direção 1 -> 3*id-2
					 * index 1 -> direção 2 -> 3*id-1
					 * index 2 -> direção 3 -> 3*id
					 * index i -> direção i+1 -> 3*id - (2 - i)
					 */
					val direction = 3 * it.id - (2 - i)
					Kb.clearRowColumn(direction - 1)
				}
			}
		}
		Kb.rightDiagonal()
		return Kb
	}
	
	private fun calculateLoadVectorBoundaryCondition(): DoubleMatrix {
		TODO("not implemented cause the initial displacements are not included")
	}
	
	private fun calculateDisplacementsVector(Kb: DoubleMatrix, P: DoubleMatrix): DoubleMatrix {
		return MatrixLib.solveSystem(Kb, P)
	}
	
	private fun calculateSupportReactionsVector(P: DoubleMatrix, D: DoubleMatrix): DoubleMatrix {
		var R = DoubleMatrix(mDegreeOfFreedom, 1)
		model.mElements.forEach {
			val eRes = ElementResult(it, D, mDegreeOfFreedom)
			R += eRes.rg
		}
		R += (P * -1.0)
		return R
	}
	
	private fun calculateResults(
		D: DoubleMatrix,
		R: DoubleMatrix
	): Pair<ArrayList<ElementResult>, ArrayList<NodeResult>> {
		val elemsResArray = ArrayList<ElementResult>()
		val nodesResArray = ArrayList<NodeResult>()
		model.mElements.forEach {
			elemsResArray.add(ElementResult(it, D, mDegreeOfFreedom))
		}
		model.mNodes.forEach {
			val displacs = DoubleArray(3)
			val reactions = DoubleArray(3)
			it.getDirections().forEachIndexed { index, direction ->
				displacs[index] = D[direction - 1, 0]
				reactions[index] = R[direction - 1, 0]
			}
			nodesResArray.add(NodeResult(it, displacs, reactions))
		}
		
		return Pair(elemsResArray, nodesResArray)
	}
	
}
