package analysis

import elems.results.ElementResult
import elems.results.NodeResult
import model.StructureModel
import utils.SupportCondition
import utils.showAsNodeDirections
import vsca.doublematrix.lib.DoubleMatrix
import vsca.doublematrix.lib.MatrixLib

/**
 * This class realize the Analysis properly said of a given structure.
 * Assemble the global matrices and makes the pos-processing
 *
 * @constructor to become possible this analysis, some data are needed
 *
 * @param mModel        data about nodes and elements of structure
 * @param mDOFByNode    number of degree of freedom by nodes of structure
 *
 * @author Vinicius Almada
 */
class Analysis(
		private val mModel: StructureModel,
		private val mDOFByNode: Int
) {

	/**
	 * The number of degree of freedom of structure
	 */
	private val mDegreeOfFreedom: Int = mModel.mNodes.size * mDOFByNode

	/**
	 * Calculate and assemble the global stiffness matrix of structure.
	 * With stiffness of elements and nodes (with springs)
	 *
	 * @return stiffness matrix
	 */
	private fun calculateStiffnessMatrix(): DoubleMatrix {
		var matrixK = DoubleMatrix(mDegreeOfFreedom)
		mModel.mElements.forEach {
			val k = it.calculateGlobalStiffnessMatrix(mDegreeOfFreedom)
			matrixK += k
		}

		mModel.mNodes.forEach {
			it.mSupportCondition.forEachIndexed { index, suppCond ->
				if (suppCond == SupportCondition.SPRING) {
					val direction = it.getDirection(index)
					matrixK[direction - 1, direction - 1] += it.mStiffValues[index]
				}
			}
		}

		return matrixK
	}

	/**
	 * Assemble the stiffness matrix with boundary conditions
	 * from the support directions definitions
	 *
	 * @param K global stiffness matrix (original)
	 * @return  stiffness matrix with boundary conditions
	 */
	private fun calculateStiffnessMatrixBoundaryCondition(K: DoubleMatrix): DoubleMatrix {
		val matrixKb = K.copy()
		mModel.mNodes.forEach {
			it.mSupportCondition.forEachIndexed { i, supCond ->
				if (supCond == SupportCondition.FIX) {
					val direction = it.getDirection(i)
					matrixKb.clearRowColumn(direction - 1)
				}
			}
		}
		matrixKb.rightDiagonal()
		return matrixKb
	}

	/**
	 * Calculate and assemble the global loads vector of structure
	 * from reaction values of loaded elements and nodal loads
	 *
	 * @return global load vector
	 */
	private fun calculateLoadVector(): DoubleMatrix {
		var vectorF = DoubleMatrix(mDegreeOfFreedom, 1)
		mModel.mNodes.forEach {
			val fn = it.calculateGlobalLoadVector(mDegreeOfFreedom)
			vectorF += fn
		}
		mModel.mElements.forEach {
			val f = it.calculateGlobalLoadVector(mDegreeOfFreedom)
			vectorF += f
		}
		return vectorF
	}

	/**
	 * Calculate the global displacements vector from global stiffness matrix and
	 * global loads vector.
	 *
	 * @param Kb    global stiffness matrix with boundary conditions
	 * @param F     global loads vector
	 *
	 * @return      global displacements vector
	 */
	private fun calculateDisplacementsVector(Kb: DoubleMatrix, F: DoubleMatrix) = MatrixLib.solveSystem(Kb, F)

	/**
	 * Calculate the support reactions vector of the structure
	 *
	 * @param F global loads vector
	 * @param D global displacements vector
	 */
	private fun calculateSupportReactionsVector(F: DoubleMatrix, D: DoubleMatrix): DoubleMatrix {
		var vectorR = DoubleMatrix(mDegreeOfFreedom, 1)
		mModel.mElements.forEach {
			val eRes = ElementResult(it, D, mDegreeOfFreedom)
			vectorR += eRes.rg
		}
		vectorR -= F
		return vectorR
	}

	/**
	 * Calculate the results of post-processing from Direct Stiffness Method:
	 * Displacements of nodes and Internal Forces of each element, the Bending Moment,
	 * Shear Force, Axial Force or Torsion Moment.
	 *
	 * @param D global displacements vector
	 * @param R support reactions vector of the structure
	 *
	 * @return  a Pair object with elements (internal forces) and nodes (displacements)
	 * @see Pair
	 * @see ElementResult
	 * @see NodeResult
	 */
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

	/**
	 * Make analysis calling the calculate methods of this class
	 *
	 * @return a Pair object with ElementResult array and a NodeResult array
	 * @see Pair
	 * @see ElementResult
	 * @see NodeResult
	 */
	fun doAnalysis(): Pair<ArrayList<ElementResult>, ArrayList<NodeResult>> {
		val matrixK = calculateStiffnessMatrix()
		println(matrixK)
		val vectorF = calculateLoadVector()
		println(vectorF)
		val matrixKb = calculateStiffnessMatrixBoundaryCondition(matrixK)
		println(matrixKb)
		val vectorD = calculateDisplacementsVector(matrixKb, vectorF)
		val vectorR = calculateSupportReactionsVector(vectorF, vectorD)

//		vectorD.showAsNodeDirections("Displacements")
//		vectorR.showAsNodeDirections("Reactions")
		return calculateResults(vectorD, vectorR)
	}

}
