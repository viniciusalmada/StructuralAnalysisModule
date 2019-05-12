package entitites

class DistributedLoad {
    var Qx: Double = 0.0
    var Qy: Double = 0.0

    var Qx_i: Double = 0.0
    var Qx_j: Double = 0.0
    var Qy_i: Double = 0.0
    var Qy_j: Double = 0.0

    var isLocalLoad: Boolean = false

    constructor(qx: Double, qy: Double, isLocalLoad: Boolean){
        this.Qx = qx
        this.Qy = qy
        this.isLocalLoad = isLocalLoad
    }

    constructor(qxi: Double, qxj: Double, qyi: Double, qyj: Double, isLocalLoad: Boolean){
        this.Qx_i = qxi
        this.Qx_j = qxj
        this.Qy_i = qyi
        this.Qy_j = qyj
        this.isLocalLoad = isLocalLoad
    }
}