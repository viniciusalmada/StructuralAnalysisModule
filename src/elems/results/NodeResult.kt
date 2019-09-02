package elems.results

import elems.Node

@Suppress("UNUSED_PARAMETER", "unused")
class NodeResult(node: Node, val displaces: DoubleArray, val reactions: DoubleArray) {
	val id: Int = node.mId
}
