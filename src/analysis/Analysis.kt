package analysis

import elems.results.ElementResult
import elems.results.NodeResult
import model.StructureModel
import utils.SupportCondition
import utils.showAsNodeDirections
import vsca.doublematrix.lib.DoubleMatrix
import vsca.doublematrix.lib.MatrixLib

class Analysis(protected val mModel: StructureModel, private val mDOFByNode: Int) {
	protected val mDegreeOfFreedom: Int = mModel.mNodes.size * mDOFByNode
	
	fun doAnalysis(): Pair<ArrayList<ElementResult>, ArrayList<NodeResult>> {
		val K = calculateStiffnessMatrix()
		val F = calculateLoadVector()
		val Kb = calculateStiffnessMatrixBoundaryCondition(K)
		val D = calculateDisplacementsVector(Kb, F)
		D.showAsNodeDirections("Displacements")
		val R = calculateSupportReactionsVector(F, D)
		R.showAsNodeDirections("Reactions")
		return calculateResults(D, R)
	}
	
	/**
	 * Calculate and assemble the global stiffness matrix of structure
	 *
	 * @return stiffness matrix
	 */
	private fun calculateStiffnessMatrix(): DoubleMatrix {
		var K = DoubleMatrix(mDegreeOfFreedom)
		mModel.mElements.forEach {
			val k = it.calculateGlobalStiffnessMatrix(mDegreeOfFreedom)
			K += k
		}
		
		mModel.mNodes.forEach {
			it.mSupportCondition.forEachIndexed { index, suppCond ->
				if (suppCond == SupportCondition.SPRING) {
					val direction = it.getDirection(index)
					K[direction - 1, direction - 1] += it.mSupportValues[index]
				}
			}
		}
		
		return K
	}
	
	/**
	 * Assemble the stiffness matrix with boundary conditions
	 * from the support directions definitions
	 *
	 * @param K - global stiffness matrix (original)
	 * @return stiffness matrix with boundary conditions
	 */
	fun calculateStiffnessMatrixBoundaryCondition(K: DoubleMatrix): DoubleMatrix {
		val Kb = K.copy()
		mModel.mNodes.forEach {
			it.mSupportCondition.forEachIndexed { i, supCond ->
				if (supCond == SupportCondition.FIX) {
					val direction = it.getDirection(i)
					Kb.clearRowColumn(direction - 1)
				}
			}
		}
		Kb.rightDiagonal()
		return Kb
	}
	
	/**
	 * Calculate and assemble the global loads vector of structure
	 *
	 * @return mLoad vector
	 */
	private fun calculateLoadVector(): DoubleMatrix {
		var F = DoubleMatrix(mDegreeOfFreedom, 1)
		mModel.mNodes.forEach {
			val fn = it.calculateGlobalLoadVector(mDegreeOfFreedom)
			F += fn
		}
		mModel.mElements.forEach {
			val f = it.calculateGlobalLoadVector(mDegreeOfFreedom)
			F += f
		}
		return F
	}
	
	private fun calculateDisplacementsVector(Kb: DoubleMatrix, F: DoubleMatrix) = MatrixLib.solveSystem(Kb, F)
	
	private fun calculateSupportReactionsVector(F: DoubleMatrix, D: DoubleMatrix): DoubleMatrix {
		var R = DoubleMatrix(mDegreeOfFreedom, 1)
		mModel.mElements.forEach {
			val eRes = ElementResult(it, D, mDegreeOfFreedom)
			R += eRes.rg
		}
		R -= F
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
			val d = DoubleArray(mDOFByNode)
			val r = DoubleArray(mDOFByNode)
			it.getDirections().forEachIndexed { index, direction ->
				d[index] = D[direction - 1, 0]
				r[index] = R[direction - 1, 0]
			}
			nodesResArray.add(NodeResult(it, d, r))
		}
		
		return Pair(elemsResArray, nodesResArray)
	}
	
}
