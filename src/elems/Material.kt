package elems

class Material(id: Int, longElastMod: Double, poisson: Double) {
	private val mPoissonRatio: Double = poisson
	val mId: Int = id
	val mLongElasticityModulus: Double = longElastMod
	val mTransverseElasticityModulus: Double = mLongElasticityModulus / (2 * (1 + mPoissonRatio))
}
