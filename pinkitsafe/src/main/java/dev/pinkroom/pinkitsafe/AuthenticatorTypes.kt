package dev.pinkroom.pinkitsafe

import androidx.annotation.IntDef
import androidx.biometric.BiometricManager

@Target(AnnotationTarget.TYPE)
@IntDef(
    flag = true,
    value = [
        BiometricManager.Authenticators.BIOMETRIC_STRONG,
        BiometricManager.Authenticators.BIOMETRIC_WEAK,
        BiometricManager.Authenticators.DEVICE_CREDENTIAL,
    ],
)
@Retention(AnnotationRetention.SOURCE)
annotation class AuthenticatorTypes