package analysis

import elems.results.ElementResult
import elems.results.NodeResult
import matrixes.DirectionsNodeVector
import matrixes.IncidenceVector
import model.StructureModel
import utils.*
import vsca.doublematrix.lib.DoubleMatrix
import vsca.doublematrix.lib.MatrixLib

/**
 * This class realize the Analysis properly said of a given structure.
 * Assemble the global matrices and makes the pos-processing
 *
 * @constructor to become possible this analysis, some data are needed
 *
 * @param mModel        data about nodes and elements of structure
 * @param mAnalysisType    type of structure to be analyzed
 *
 * @author Vinicius Almada
 */
class Analysis(
        private val mModel: StructureModel,
        mAnalysisType: String
) {

    /**
     * The number of degree of freedom of structure
     */
    private val mDegreeOfFreedom: Int =
            mModel.mNodes.size * when (mAnalysisType) {
                ANALYSIS_BEAM -> DOF_NODE_BEAM
                ANALYSIS_GRILLAGE -> DOF_NODE_GRILLAGE
                ANALYSIS_PLANE_FRAME -> DOF_NODE_PLANE_FRAME
                ANALYSIS_PLANE_TRUSS -> DOF_NODE_PLANE_TRUSS
                else -> 0
            }

    /**
     * Calculate and assemble the global stiffness matrix of structure.
     * With stiffness of elements and nodes (with springs)
     *
     * @return stiffness matrix
     */
    private fun calculateStiffnessMatrix(): DoubleMatrix {
        val matrixK = DoubleMatrix(mDegreeOfFreedom)
        mModel.mElements.forEach {
            val localK = it.localStiffnessMatrix()
            val vectorI = IncidenceVector(it).vector()
            vectorI.forEachIncidenceToMatrix { row, col, valRow, valCol ->
                matrixK[valRow - 1, valCol - 1] += localK[row, col]
            }
        }

        mModel.mNodes.forEach {
            it.mSupportCondition.forEachIndexed { index, suppCond ->
                if (suppCond == SupportCondition.SPRING) {
                    val direction = DirectionsNodeVector(it).direction(index)
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
            it.mSupportCondition.forEachIndexed { index, supCond ->
                if (supCond == SupportCondition.FIX) {
                    val direction = DirectionsNodeVector(it).direction(index)
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
    private fun calculateLoadVector(): DoubleArray {
        val vectorF = DoubleArray(mDegreeOfFreedom)
        mModel.mElements.forEach {
            val localF = it.localLoadsVector()
            val vectorI = IncidenceVector(it).vector()
            vectorI.forEachIncidenceToVector { row, rowValue ->
                vectorF[rowValue - 1] += localF[row]
            }
        }

        mModel.mNodes.forEach {
            val localF = it.localLoadsVector()
            val vectorI = IncidenceVector(it).vector()
            vectorI.forEachIncidenceToVector { row, rowValue ->
                vectorF[rowValue - 1] += localF[row]
            }
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
    private fun calculateDisplacementsVector(Kb: DoubleMatrix, F: DoubleArray) = MatrixLib.solveSystem(Kb, F).toDoubleArray()

    /**
     * Calculate the support reactions vector of the structure
     *
     * @param F global loads vector
     * @param D global displacements vector
     */
    private fun calculateSupportReactionsVector(F: DoubleArray, D: DoubleArray): DoubleArray {
        var vectorR = DoubleArray(mDegreeOfFreedom)
        mModel.mElements.forEach {
            val eRes = ElementResult(it, D)
            val vectorI = IncidenceVector(it).vector()
            vectorI.forEachIncidenceToVector { row, rowValue ->
                vectorR[rowValue - 1] += eRes.rg[row]
            }
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
    private fun calculateResults(D: DoubleArray, R: DoubleArray)
            : Pair<ArrayList<ElementResult>, ArrayList<NodeResult>> {
        val elemsResArray = ArrayList<ElementResult>()
        val nodesResArray = ArrayList<NodeResult>()
        mModel.mElements.forEach {
            elemsResArray.add(ElementResult(it, D))
        }
        mModel.mNodes.forEach {
            val vectorI = IncidenceVector(it).vector()
            val d = D.subVectorFromIncidence(vectorI)
            val r = R.subVectorFromIncidence(vectorI)
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
        matrixK.showNullValues()
        val vectorF = calculateLoadVector()
        val matrixKb = calculateStiffnessMatrixBoundaryCondition(matrixK)
        val vectorD = calculateDisplacementsVector(matrixKb, vectorF)
        val vectorR = calculateSupportReactionsVector(vectorF, vectorD)

        vectorD.showAsNodeDirections("Displacements")
        vectorR.showAsNodeDirections("Reactions")
        return calculateResults(vectorD, vectorR)
    }

}
