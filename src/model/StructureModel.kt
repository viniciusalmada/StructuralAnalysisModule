package model

import analysis.AnalysisFrame2D
import entitites.Element
import entitites.Material
import entitites.Node
import entitites.Section
import utils.SupportCondition.*

/**
 * <b>Units</b>
 * Force = kN
 * Rotation Stiffness = kNm/rad
 * Elasticity Module = kN/m²
 */

class StructureModel {
    val mNodes = ArrayList<Node>()
    val mElements = ArrayList<Element>()
    val mMaterials = ArrayList<Material>()
    val mSections = ArrayList<Section>()
    val mAnalisysType = AnalysisFrame2D(this)

    init {
        /*///////////////
        // N O D E S //
        ///////////////
        val n1 = Node(1, 0.0, 0.0, arrayOf(FIX, FIX, FIX))
        val n2 = Node(2, 12.0, 0.0, arrayOf(FIX, FIX, SPRING), doubleArrayOf(0.0, 0.0, 80_000.0))
        val n3 = Node(3, 0.0, 2.0, arrayOf(FREE, FREE, FREE), doubleArrayOf(20.0, 0.0, 0.0))
        val n4 = Node(4, 0.0, 6.0, arrayOf(FREE, FREE, FREE), doubleArrayOf(20.0, 0.0, 0.0))
        val n5 = Node(5, 12.0, 7.0)
        val n6 = Node(6, 12.0, 11.0)
        mNodes.add(n1)
        mNodes.add(n2)
        mNodes.add(n3)
        mNodes.add(n4)
        mNodes.add(n5)
        mNodes.add(n6)
        mNodes.forEach { println(it) }


        ///////////////////////
        // M A T E R I A L S //
        ///////////////////////
        val m1 = Material(1, 2e8, 0.2)
        mMaterials.add(m1)


        /////////////////////
        // S E C T I O N S //
        /////////////////////
        val s1 = Section(1, 0.0103279, 0.7745966)
        mSections.add(s1)


        /////////////////////
        // E L E M E N T S //
        /////////////////////
        val e1 = Element(1, n1, n3, m1, s1,0.0,false, false, false)
        val e2 = Element(2, n2, n5, m1, s1,0.0,false, false, false)
        val e3 = Element(3, n3, n4, m1, s1,0.0,false, false, true)
        val e4 = Element(4, n3, n5, m1, s1, -12.0, false, false,false)
        val e5 = Element(5, n4, n6, m1, s1, -12.0, false, true,false)
        val e6 = Element(6, n6, n5, m1, s1,0.0,false, false, false)
        mElements.add(e1)
        mElements.add(e2)
        mElements.add(e3)
        mElements.add(e4)
        mElements.add(e5)
        mElements.add(e6)
        mElements.forEach { println(it) }*/


        // Exemplo simples que deu certo
        val n1 = Node(1, 7.0, -1.0, arrayOf(FIX, FIX, SPRING), doubleArrayOf(0.0, 0.0, 80_000.0))
        val n2 = Node(2, 0.0, 0.0, arrayOf(FIX, FIX, FREE))
//        val n3 = Node(3, 0.0, 4.0, arrayOf(FREE, FREE, FREE), doubleArrayOf(50.0, -10.0, 0.0))
//        val n4 = Node(4, 7.0, 7.0)
//        val n3 = Node(3, 0.0, 4.0, arrayOf(FREE, FREE, FREE), doubleArrayOf(54.1361, -65.3456, -79.9656))
//        val n4 = Node(4, 7.0, 7.0, arrayOf(FREE, FREE, FREE), doubleArrayOf(-4.1361, -36.0436, 0.0))
        val n3 = Node(3, 0.0, 4.0, arrayOf(FREE, FREE, FREE), doubleArrayOf(50.0, -55.6946, 0.0))
        val n4 = Node(4, 7.0, 7.0, arrayOf(FREE, FREE, FREE), doubleArrayOf(0.0, -45.6946, 0.0))
        mNodes.addAll(arrayOf(n1, n2, n3, n4))

        val m1 = Material(1, 2e8, 0.2)
        mMaterials.add(m1)

        val s1 = Section(1, 0.0103279, 0.7745966)
        mSections.add(s1)

        val e1 = Element(1, n2, n3, m1, s1, hasHingeBegin = false, hasHingeEnd = true)
        val e2 = Element(2, n3, n4, m1, s1, hasHingeBegin = true, hasHingeEnd = true)
        val e3 = Element(3, n4, n1, m1, s1, hasHingeBegin = true, hasHingeEnd = false)
        mElements.addAll(arrayOf(e1, e2, e3))


        val results = mAnalisysType.doAnalysis()
    }
}