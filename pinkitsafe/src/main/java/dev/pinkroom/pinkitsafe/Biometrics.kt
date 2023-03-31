package dev.pinkroom.pinkitsafe

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class Biometrics private constructor() {

    private lateinit var context: Context
    private lateinit var fragment: Fragment
    private lateinit var fragmentActivity: FragmentActivity

    constructor(context: Context) : this() {
        this.context = context
        this.fragmentActivity = context as FragmentActivity
    }

    constructor(fragment: Fragment) : this() {
        this.context = fragment.requireContext()
        this.fragment = fragment
    }

    constructor(fragmentActivity: FragmentActivity) : this() {
        this.context = fragmentActivity
        this.fragmentActivity = fragmentActivity
    }

    fun authenticate(
        title: String,
        subtitle: String? = null,
        description: String? = null,
        negativeButtonText: String = context.getString(android.R.string.cancel),
        allowedAuthenticators: @AuthenticatorTypes Int = Authenticators.BIOMETRIC_STRONG,
        error: (errorCode: Int, errString: CharSequence) -> Unit = { _, _ -> },
        success: () -> Unit,
    ) {
        val biometricPrompt = buildBiometricPrompt(success, error)
        val promptInfo =
            buildPromptInfo(title, subtitle, description, negativeButtonText, allowedAuthenticators)
        biometricPrompt.authenticate(promptInfo)
    }

    fun isAvailable(
        allowedAuthenticators: @AuthenticatorTypes Int = Authenticators.BIOMETRIC_STRONG
    ) = canAuthenticate(allowedAuthenticators) == BiometricManager.BIOMETRIC_SUCCESS

    private fun canAuthenticate(allowedAuthenticators: @AuthenticatorTypes Int) =
        BiometricManager.from(context).canAuthenticate(allowedAuthenticators)

    private fun buildBiometricPrompt(
        success: () -> Unit,
        error: (errorCode: Int, errString: CharSequence) -> Unit,
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(context)
        return if (::fragment.isInitialized) {
            BiometricPrompt(
                fragment,
                executor,
                authenticationCallback(success, error),
            )
        } else {
            BiometricPrompt(
                fragmentActivity,
                executor,
                authenticationCallback(success, error),
            )
        }
    }

    private fun authenticationCallback(
        success: () -> Unit,
        error: (errorCode: Int, errString: CharSequence) -> Unit,
    ): BiometricPrompt.AuthenticationCallback {
        return object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                error(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                success()
            }
        }
    }

    private fun buildPromptInfo(
        title: String,
        subtitle: String?,
        description: String?,
        negativeButtonText: String,
        allowedAuthenticators: @AuthenticatorTypes Int,
    ) = BiometricPrompt.PromptInfo.Builder().apply {
        setTitle(title)
        setSubtitle(subtitle)
        setDescription(description)
        setConfirmationRequired(false)
        setNegativeButtonText(negativeButtonText)
        setAllowedAuthenticators(allowedAuthenticators)
    }.build()
}