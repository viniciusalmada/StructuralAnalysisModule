package elems

class Section(id: Int, area: Double, inertiaZ: Double, inertiaY: Double) {
	val mId: Int = id
	val mArea: Double = area
	val mInertiaMomentZ: Double = inertiaZ
	val mInertiaMomentY: Double = inertiaY
	
	fun getPolarInertiaMoment() = this.mInertiaMomentZ + this.mInertiaMomentY
	
	/*fun getArea(): Double = width * height
	
	fun getInertiaMoment(): Double = (width * pow(height, 3.0)) / 12.0*/
}
