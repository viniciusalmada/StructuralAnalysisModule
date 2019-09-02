package model

import analysis.Analysis
import com.google.gson.Gson
import elems.*
import utils.*
import java.io.File
import java.io.FileReader
import java.lang.Boolean.TRUE

class StructureModel(file: File) {

    private val mAnalysisType: Analysis

    val mNodes: HashSet<Node> = HashSet()
    val mElements: HashSet<Element> = HashSet()
    val mMaterials: HashSet<Material> = HashSet()
    val mSections: HashSet<Section> = HashSet()
    val mDistributedLoads: HashSet<DistributedLoad> = HashSet()

    init {
        val dataModel = Gson().fromJson(FileReader(file), StructureDataModel::class.java)
        loadModel(dataModel)
        mAnalysisType = Analysis(this, dataModel.analysis)
        val res = mAnalysisType.doAnalysis()
        val gson = Gson()
        val str = gson.toJson(res)
        println(str)
    }

    private fun loadModel(dataModel: StructureDataModel) {
        for (m in dataModel.materials) {
            val material = Material(m.id, m.longElastModulus, m.poissonRatio)
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

        when (dataModel.analysis) {
            ANALYSIS_BEAM -> createBeamElements(dataModel)
            ANALYSIS_GRILLAGE -> createGrillageElements(dataModel)
            ANALYSIS_PLANE_FRAME -> createPlaneFrameElements(dataModel)
            ANALYSIS_PLANE_TRUSS -> createPlaneTrussElements(dataModel)
        }
    }

    private fun createPlaneTrussElements(dataModel: StructureDataModel) {
        for (n in dataModel.nodes) {
            val node = Node(n.id, n.x, n.y, 0.0, n.suppCond, n.loadValues, n.stiffValues, StructureType.PLANE_TRUSS)
            mNodes.add(node)
        }

        for (e in dataModel.elements) {
            val element = Element(e.id, e.nodeId1, e.nodeId2, e.materialId, e.sectionId, TRUE, TRUE, e.loadId, this, StructureType.PLANE_TRUSS)
            mElements.add(element)
        }
    }

    private fun createPlaneFrameElements(dataModel: StructureDataModel) {
		for (n in dataModel.nodes) {
            val node = Node(n.id, n.x, n.y, 0.0, n.suppCond, n.loadValues, n.stiffValues, StructureType.PLANE_FRAME)
			mNodes.add(node)
		}

		for (e in dataModel.elements) {
            val element = Element(e.id, e.nodeId1, e.nodeId2, e.materialId, e.sectionId, e.hasHingedBegin, e.hasHingedEnd, e.loadId, this, StructureType.PLANE_FRAME)
            mElements.add(element)
		}
	}

    private fun createGrillageElements(dataModel: StructureDataModel) {
		for (n in dataModel.nodes) {
            val node = Node(n.id, n.x, 0.0, n.z, n.suppCond, n.loadValues, n.stiffValues, StructureType.GRILLAGE)
			mNodes.add(node)
		}

		for (e in dataModel.elements) {
            val element = Element(e.id, e.nodeId1, e.nodeId2, e.materialId, e.sectionId, e.hasHingedBegin, e.hasHingedEnd, e.loadId, this, StructureType.GRILLAGE)
            mElements.add(element)
		}
	}

    private fun createBeamElements(dataModel: StructureDataModel) {
        for (n in dataModel.nodes) {
            val node = Node(n.id, n.x, 0.0, 0.0, n.suppCond, n.loadValues, n.stiffValues, StructureType.BEAM)
            mNodes.add(node)
        }

        for (e in dataModel.elements) {
            val element = Element(e.id, e.nodeId1, e.nodeId2, e.materialId, e.sectionId, e.hasHingedBegin, e.hasHingedEnd, e.loadId, this, StructureType.BEAM)
            mElements.add(element)
        }
    }
}