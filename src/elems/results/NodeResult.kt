package elems.results

import elems.NodeAbs

@Suppress("UNUSED_PARAMETER", "unused")
class NodeResult(node: NodeAbs, val displacs: DoubleArray, val reactions: DoubleArray) {
	val id: Int = node.mId
}
