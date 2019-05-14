package model

import analysis.AnalysisFrame2D
import entitites.*
import utils.SupportCondition.*

class StructureModel {
	val mNodes = HashSet<Node>()
	val mElements = HashSet<Element>()
	val mMaterials = HashSet<Material>()
	val mSections = HashSet<Section>()
	val mDistributedLoad = HashSet<DistributedLoad>()
	val mAnalisysType = AnalysisFrame2D(this)
	
	init {
		/*mNodes.add(Node(1, 0.0, 0.0, arrayOf(FIX, FIX, FIX)))
		mNodes.add(Node(2, 4.0, 0.0, arrayOf(FIX, FIX, FIX)))
		mNodes.add(Node(3, 8.0, 0.0, arrayOf(FIX, FIX, FIX)))
		mNodes.add(Node(4, 4.0, 4.0, arrayOf(FREE, FREE, FREE), doubleArrayOf(600.0, -1000.0, 300.0)))
		mNodes.add(Node(5, 8.0, 4.0, arrayOf(FREE, FREE, FREE), doubleArrayOf(0.0, -800.0, 0.0)))
		
		mMaterials.add(Material(1, 1.5e7, 0.2))
		
		mSections.add(Section(1, 0.2, 0.6))
		mSections.add(Section(2, 0.2, 0.3))
		
		mElements.add(Element(1, 1, 4, 1, 1, model = this))
		mElements.add(Element(2, 2, 4, 1, 1, model = this))
		mElements.add(Element(3, 3, 5, 1, 1, model = this))
		mElements.add(Element(4, 4, 5, 1, 2, model = this))*/
		
		val n1 = Node(1, 0.0, 0.0, arrayOf(FIX, FIX, FIX))
		val n2 = Node(2, 12.0, 0.0, arrayOf(FIX, FIX, SPRING), doubleArrayOf(0.0, 0.0, 80_000.0))
		val n3 = Node(3, 0.0, 2.0, arrayOf(FREE, FREE, FREE), doubleArrayOf(20.0, 0.0, 0.0))
		val n4 = Node(4, 0.0, 6.0, arrayOf(FREE, FREE, FREE), doubleArrayOf(20.0, 0.0, 0.0))
		val n5 = Node(5, 12.0, 7.0)
		val n6 = Node(6, 12.0, 11.0)
		mNodes.addAll(listOf(n1, n2, n3, n4, n5, n6))
		
		val m1 = Material(1, 2e8, 0.2)
		mMaterials.add(m1)
		
		val s1 = Section(1, 0.0103279, 0.7745966)
		mSections.add(s1)
		
		val l1 = DistributedLoad(1, 0.0, -12.0, false)
		val l2 = DistributedLoad(2, -10.0, -20.0, -10.0, -20.0, false)
		mDistributedLoad.addAll(listOf(l1, l2))
		
		val e1 = Element(1, 1, 3, 1, 1, model = this)
		val e2 = Element(2, 2, 5, 1, 1, model = this)
		val e3 = Element(3, 3, 4, 1, 1, hasHingeEnd = true, model = this)
		val e4 = Element(4, 3, 5, 1, 1, loadId = 1, model = this)
		val e5 = Element(5, 4, 6, 1, 1, hasHingeBegin = true, loadId = 1, model = this)
		val e6 = Element(6, 6, 5, 1, 1, model = this)
		mElements.addAll(listOf(e1, e2, e3, e4, e5, e6))
		
		@Suppress("UNUSED_VARIABLE") val res = mAnalisysType.doAnalysis()
	}
}