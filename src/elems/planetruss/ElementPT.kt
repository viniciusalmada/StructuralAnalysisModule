package elems.planetruss

import elems.Element
import model.StructureModel
import utils.StructureType

class ElementPT(
	id: Int,
	nodeId_i: Int,
	nodeId_j: Int,
	materialId: Int,
	sectionId: Int,
	loadId: Int,
	model: StructureModel
) : Element(id, nodeId_i, nodeId_j, materialId, sectionId, true, true, loadId, model) {
	
	
	override fun getType(): StructureType = StructureType.PLANE_TRUSS
	
}