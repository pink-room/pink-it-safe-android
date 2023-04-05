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

@Target(AnnotationTarget.TYPE)
@IntDef(
    BiometricManager.BIOMETRIC_STATUS_UNKNOWN,
    BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
    BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED
)
@Retention(AnnotationRetention.SOURCE)
annotation class AuthenticationErrorStatus