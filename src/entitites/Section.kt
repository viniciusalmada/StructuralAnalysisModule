package entitites

import java.lang.Math.pow

class Section(
    val id: Int,
    val width: Double,
    val height: Double
) {

    fun getArea(): Double = /*width * height*/ 0.008

    fun getInertiaMoment(): Double = /*(width * pow(height, 3.0)) / 12.0*/ 0.0004
}
