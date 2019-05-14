import model.StructureModel

fun main() {
    val time1 = System.currentTimeMillis() / (1000.0)
    StructureModel()
    val time2 = System.currentTimeMillis() / (1000.0)
    println("time == ${time2 - time1}")
}