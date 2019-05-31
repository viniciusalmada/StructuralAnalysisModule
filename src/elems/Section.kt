package elems

import java.lang.Math.pow

class Section(
    val id: Int,
    val width: Double,
    val height: Double
) {
	
	fun getArea(): Double = width * height
	
	fun getInertiaMoment(): Double = (width * pow(height, 3.0)) / 12.0
}
