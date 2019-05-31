package model

data class StructureDataModel(
	val analysis: String,
	val nodes: List<NodeData>,
	val materials: List<MaterialData>,
	val sections: List<SectionData>,
	val distributedLoads: List<DistributedLoad>,
	val elements: List<ElementData>
) {
	
	data class NodeData(
		val id: Int,
		val x: Double,
		val y: Double,
		val suppCond: Array<String>,
		val suppValues: DoubleArray
	)
	
	data class MaterialData(
		val id: Int,
		val longitudinalElasticityModule: Double,
		val poissonCoefficient: Double
	)
	
	data class SectionData(
		val id: Int,
		val width: Double,
		val height: Double
	)
	
	data class DistributedLoad(
		val id: Int,
		val qxi: Double,
		val qxj: Double,
		val qyi: Double,
		val qyj: Double,
		val isLocalLoad: Boolean
	)
	
	data class ElementData(
		val id: Int,
		val nodeId_i: Int,
		val nodeId_j: Int,
		val materialId: Int,
		val sectionId: Int,
		val hasHingedBegin: Boolean,
		val hasHingedEnd: Boolean,
		val loadId: Int
	)
}