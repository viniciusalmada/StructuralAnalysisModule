package analysis

import elems.results.ElementResult
import elems.results.NodeResult
import model.StructureModel
import utils.showAsNodeDirections
import vsca.doublematrix.lib.DoubleMatrix
import vsca.doublematrix.lib.MatrixLib

abstract class Analysis(protected val mModel: StructureModel, private val mDOFByNode: Int) {
	private val mDegreeOfFreedom: Int = mModel.mNodes.size * mDOFByNode
	
	/**
	 * Assemble the stiffness matrix with boundary conditions
	 * from the support directions definitions
	 *
	 * @param K - global stiffness matrix (original)
	 * @return stiffness matrix with boundary conditions
	 */
	abstract fun calculateStiffnessMatrixBoundaryCondition(K: DoubleMatrix): DoubleMatrix
	
	fun doAnalysis(): Pair<ArrayList<ElementResult>, ArrayList<NodeResult>> {
		val K = calculateStiffnessMatrix()
		val P = calculateLoadVector()
		P.showAsNodeDirections()
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
	private fun calculateStiffnessMatrix(): DoubleMatrix {
		var stiffMatrix = DoubleMatrix(mDegreeOfFreedom)
		mModel.mElements.forEach {
			val stiffMatrixOfElement = it.calculateGlobalStiffnessMatrix(mDegreeOfFreedom)
			stiffMatrix += stiffMatrixOfElement
		}
		
		mModel.mNodes.forEach {
			val stiffMatrixOfNode = it.calculateStiffnessMatrix(mDegreeOfFreedom)
			stiffMatrix += stiffMatrixOfNode
		}
		
		return stiffMatrix
	}
	
	/**
	 * Calculate and assemble the global loads vector of structure
	 *
	 * @return mLoad vector
	 */
	private fun calculateLoadVector(): DoubleMatrix {
		var loadVector = DoubleMatrix(mDegreeOfFreedom, 1)
		mModel.mNodes.forEach {
			val loadVectorOfNode = it.calculateGlobalLoadVector(mDegreeOfFreedom)
			loadVector += loadVectorOfNode
		}
		mModel.mElements.forEach {
			val loadVectorOfElement = it.calculateGlobalLoadVector(mDegreeOfFreedom)
			loadVector += loadVectorOfElement
		}
		return loadVector
	}
	
	private fun calculateDisplacementsVector(Kb: DoubleMatrix, P: DoubleMatrix): DoubleMatrix {
		return MatrixLib.solveSystem(Kb, P)
	}
	
	private fun calculateSupportReactionsVector(P: DoubleMatrix, D: DoubleMatrix): DoubleMatrix {
		var R = DoubleMatrix(mDegreeOfFreedom, 1)
		mModel.mElements.forEach {
			val eRes = ElementResult(it, D, mDegreeOfFreedom)
			R += eRes.rg
		}
		R += (P * -1.0)
		return R
	}
	
	private fun calculateResults(D: DoubleMatrix, R: DoubleMatrix)
			: Pair<ArrayList<ElementResult>, ArrayList<NodeResult>> {
		val elemsResArray = ArrayList<ElementResult>()
		val nodesResArray = ArrayList<NodeResult>()
		mModel.mElements.forEach {
			elemsResArray.add(ElementResult(it, D, mDegreeOfFreedom))
		}
		mModel.mNodes.forEach {
			val displacs = DoubleArray(mDOFByNode)
			val reactions = DoubleArray(mDOFByNode)
			it.getDirections().forEachIndexed { index, direction ->
				displacs[index] = D[direction - 1, 0]
				reactions[index] = R[direction - 1, 0]
			}
			nodesResArray.add(NodeResult(it, displacs, reactions))
		}
		
		return Pair(elemsResArray, nodesResArray)
	}
	
}
