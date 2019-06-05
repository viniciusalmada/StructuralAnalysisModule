package model

import analysis.Analysis
import analysis.AnalysisGrillage
import analysis.AnalysisPlaneFrame
import analysis.AnalysisPlaneTruss
import com.google.gson.Gson
import elems.*
import elems.grillage.ElementG
import elems.grillage.NodeG
import elems.planeframe.ElementPF
import elems.planeframe.NodePF
import elems.planetruss.ElementPT
import elems.planetruss.NodePT
import utils.ANALYSIS_GRILLAGE
import utils.ANALYSIS_PLANE_FRAME
import utils.ANALYSIS_PLANE_TRUSS
import java.io.File
import java.io.FileReader

class StructureModel(file: File) {
	val mNodes = HashSet<NodeAbs>()
	val mElements = HashSet<ElementAbs>()
	val mMaterials = HashSet<Material>()
	val mSections = HashSet<Section>()
	val mDistributedLoads = HashSet<DistributedLoad>()
	val mAnalisysType: Analysis
	
	init {
		val dataModel = Gson().fromJson(FileReader(file), StructureDataModel::class.java)
		
		when (dataModel.analysis) {
			ANALYSIS_PLANE_FRAME -> {
				loadPFModel(dataModel)
				mAnalisysType = AnalysisPlaneFrame(this)
			}
			ANALYSIS_PLANE_TRUSS -> {
				loadPTModel(dataModel)
				mAnalisysType = AnalysisPlaneTruss(this)
			}
			ANALYSIS_GRILLAGE -> {
				loadGModel(dataModel)
				mAnalisysType = AnalysisGrillage(this)
			}
			else -> mAnalisysType = AnalysisPlaneTruss(this)
		}
		val res = mAnalisysType.doAnalysis()
		val gson = Gson()
		val str = gson.toJson(res)
		println(str)
	}
	
	private fun loadPFModel(dataModel: StructureDataModel) {
		for (n in dataModel.nodes) {
			val node = NodePF(n.id, n.x, n.y, n.suppCond, n.suppValues)
			mNodes.add(node)
		}
		
		for (m in dataModel.materials) {
			val material = Material(m.id, m.longElastModulus, m.poissonRatio, m.transElastModulus)
			mMaterials.add(material)
		}
		
		for (s in dataModel.sections) {
			val section = Section(s.id, s.area, s.inertiaMomentZ, s.inertiaMomentY)
			mSections.add(section)
		}
		
		for (dl in dataModel.distributedLoads) {
			val loads = DistributedLoad(dl.id, dl.qxi, dl.qxj, dl.qyi, dl.qyj, dl.isLocalLoad)
			mDistributedLoads.add(loads)
		}
		
		for (e in dataModel.elements) {
			val elem = ElementPF(
				e.id,
				e.nodeId_i,
				e.nodeId_j,
				e.materialId,
				e.sectionId,
				e.hasHingedBegin,
				e.hasHingedEnd,
				e.loadId,
				this
			)
			mElements.add(elem)
		}
	}
	
	private fun loadGModel(dataModel: StructureDataModel) {
		for (n in dataModel.nodes) {
			val node = NodeG(n.id, n.z, n.x, n.suppCond, n.suppValues)
			mNodes.add(node)
		}
		
		for (m in dataModel.materials) {
			val material = Material(m.id, m.longElastModulus, m.poissonRatio, m.transElastModulus)
			mMaterials.add(material)
		}
		
		for (s in dataModel.sections) {
			val section = Section(s.id, s.area, s.inertiaMomentZ, s.inertiaMomentY)
			mSections.add(section)
		}
		
		for (dl in dataModel.distributedLoads) {
			val loads = DistributedLoad(dl.id, dl.qxi, dl.qxj, dl.qyi, dl.qyj, dl.isLocalLoad)
			mDistributedLoads.add(loads)
		}
		
		for (e in dataModel.elements) {
			val elem = ElementG(
				e.id,
				e.nodeId_i,
				e.nodeId_j,
				e.materialId,
				e.sectionId,
				e.hasHingedBegin,
				e.hasHingedEnd,
				e.loadId,
				this
			)
			mElements.add(elem)
		}
	}
	
	private fun loadPTModel(dataModel: StructureDataModel) {
		for (n in dataModel.nodes) {
			val node = NodePT(n.id, n.x, n.y, n.suppCond, n.suppValues)
			mNodes.add(node)
		}
		
		for (m in dataModel.materials) {
			val material = Material(m.id, m.longElastModulus, m.poissonRatio, m.transElastModulus)
			mMaterials.add(material)
		}
		
		for (s in dataModel.sections) {
			val section = Section(s.id, s.area, s.inertiaMomentZ, s.inertiaMomentY)
			mSections.add(section)
		}
		
		for (dl in dataModel.distributedLoads) {
			val loads = DistributedLoad(dl.id, dl.qxi, dl.qxj, dl.qyi, dl.qyj, dl.isLocalLoad)
			mDistributedLoads.add(loads)
		}
		
		for (e in dataModel.elements) {
			val elem = ElementPT(
				e.id,
				e.nodeId_i,
				e.nodeId_j,
				e.materialId,
				e.sectionId,
				e.loadId,
				this
			)
			mElements.add(elem)
		}
	}
}