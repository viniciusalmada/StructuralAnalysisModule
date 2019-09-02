import model.StructureModel
import java.io.File

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            StructureModel(File("test2PF.json"))
        }
    }
}