package elems.planeframe

import elems.Element
import model.StructureModel
import utils.StructureType

class ElementPF(
		id: Int,
		nodeId_i: Int,
		nodeId_j: Int,
		materialId: Int,
		sectionId: Int,
		hasHingeBegin: Boolean,
		hasHingeEnd: Boolean,
		loadId: Int,
		model: StructureModel
) : Element(id, nodeId_i, nodeId_j, materialId, sectionId, hasHingeBegin, hasHingeEnd, loadId, model) {

	override fun getType(): StructureType = StructureType.PLANE_FRAME

}