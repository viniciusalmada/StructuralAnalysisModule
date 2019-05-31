package analysis

import model.StructureModel
import utils.DOF_NODE_PLANE_TRUSS
import utils.INVALID_DIRECTION
import utils.SupportCondition
import vsca.doublematrix.lib.DoubleMatrix

class AnalysisPlaneTruss(model: StructureModel) : Analysis(model, DOF_NODE_PLANE_TRUSS) {
	
	override fun calculateStiffnessMatrixBoundaryCondition(K: DoubleMatrix): DoubleMatrix {
		val Kb = K.copy()
		mModel.mNodes.forEach {
			it.mSupportCondition.forEachIndexed { i, supCond ->
				if (supCond == SupportCondition.FIX) {
					val direction = when (i) {
						0 -> DOF_NODE_PLANE_TRUSS * it.mId - 1
						1 -> DOF_NODE_PLANE_TRUSS * it.mId
						else -> INVALID_DIRECTION
					}
					Kb.clearRowColumn(direction - 1)
				}
			}
		}
		Kb.rightDiagonal()
		return Kb
	}
}