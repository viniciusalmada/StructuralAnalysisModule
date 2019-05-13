package analysis

import entitites.ElementResult
import entitites.NodeResult
import model.StructureModel
import utils.SupportCondition.FIX
import utils.showAsNodeDirections
import vsca.doublematrix.lib.DoubleMatrix
import vsca.doublematrix.lib.MatrixLib

class AnalysisFrame2D(private val model: StructureModel) {
	private var mDegreeOfFreedom: Int = 0
	
	fun doAnalysis(): Pair<Array<ElementResult>, Array<NodeResult>> {
		mDegreeOfFreedom = model.mNodes.size * 3
		val K = calculateStiffnessMatrix()
		val P = calculateLoadVector()
		val Kb = calculateStiffnessMatrixBoundaryCondition(K)
		val D = calculateDisplacementsVector(Kb, P)
		D.showAsNodeDirections()
		
		TODO()
	}
	
	/**
	 * Calculate and assemble the global stiffness matrix of structure
	 *
	 * @return stiffness matrix
	 */
	private fun calculateStiffnessMatrix(): DoubleMatrix {
		var stiffMatrix = DoubleMatrix(mDegreeOfFreedom)
		model.mElements.forEach {
			val stiffMatrixOfElement = it.calculateGlobalStiffnessMatrix(mDegreeOfFreedom)
			stiffMatrix += stiffMatrixOfElement
		}
		model.mNodes.forEach {
			val stiffMatrixOfNode = it.calculateStiffnessMatrix(mDegreeOfFreedom)
			stiffMatrix += stiffMatrixOfNode
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
	
	private fun calculateSupportReactionsVector(): DoubleMatrix {
		TODO()
	}
	
	private fun calculateResults(): Pair<Array<ElementResult>, Array<NodeResult>> {
		TODO()
	}
	
}
