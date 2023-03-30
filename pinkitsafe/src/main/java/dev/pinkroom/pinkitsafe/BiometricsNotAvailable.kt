package dev.pinkroom.pinkitsafe

data class BiometricsNotAvailable(
    override val message: String? = "Biometric sensors unavailable"
) : Throwable()