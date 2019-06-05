package elems

class Material(id: Int, longElastMod: Double, poisson: Double, transvElastModulus: Double) {
	val mId: Int = id
	val mLongElasticityModulus: Double = longElastMod
	val mPoissonRatio: Double
	val mTransvElasticityModulus: Double
	
	init {
		if (poisson == 0.0 && transvElastModulus != 0.0) {
			mTransvElasticityModulus = transvElastModulus
			mPoissonRatio =
				mLongElasticityModulus / (2 * mTransvElasticityModulus) - 1
		} else if (poisson != 0.0 && transvElastModulus == 0.0) {
			mPoissonRatio = poisson
			mTransvElasticityModulus =
				mLongElasticityModulus / (2 * (1 + mPoissonRatio))
		} else {
			mPoissonRatio = 0.0
			mTransvElasticityModulus = 0.0
			throw IllegalArgumentException("Poisson Ratio and G should not be null or zero.")
		}
	}
}
