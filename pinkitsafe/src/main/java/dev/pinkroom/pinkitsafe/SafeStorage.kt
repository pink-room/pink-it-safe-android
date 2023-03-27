package dev.pinkroom.pinkitsafe

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.KeyScheme
import com.google.gson.Gson
import java.security.KeyStore

class SafeStorage(
    context: Context,
    keyScheme: KeyScheme = KeyScheme.AES256_GCM,
    prefKeyEncryptionScheme: EncryptedSharedPreferences.PrefKeyEncryptionScheme = EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    prefValueEncryptionScheme: EncryptedSharedPreferences.PrefValueEncryptionScheme = EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    authenticationRequired: Boolean = false,
    userAuthenticationValidityDurationSeconds: Int = 1,
    requestStrongBoxBacked: Boolean = authenticationRequired,
    fileName: String = if (authenticationRequired) "auth_safe_storage" else "safe_storage",
) {

    @PublishedApi
    internal val gson = Gson()

    fun <T> save(key: String, value: T) =
        preferences.edit().putString(key, gson.toJson(value)).apply()

    inline fun <reified T> get(key: String): T =
        gson.fromJson(preferences.getString(key, null) ?: "")

    fun clear() = preferences.edit().clear().apply()

    private val masterKey: MasterKey by lazy {
        runCatching { KeyStore.getInstance(KeyStore.getDefaultType()).apply { load(null) } }
        MasterKey.Builder(context).apply {
            setKeyScheme(keyScheme)
            setUserAuthenticationRequired(
                authenticationRequired,
                userAuthenticationValidityDurationSeconds,
            )
            setRequestStrongBoxBacked(requestStrongBoxBacked)
        }.build()
    }

    // Review: Doing some weird stuff here due to https://issuetracker.google.com/issues/176215143
    @PublishedApi
    internal val preferences by lazy {
        // Why 50? Why didn't Google fix this issue yet? There are answers that we simply don't have.
        repeat(50) {
            runCatching {
                KeyStore.getInstance(KeyStore.getDefaultType()).apply { load(null) }
                EncryptedSharedPreferences.create(
                    context,
                    fileName,
                    masterKey,
                    prefKeyEncryptionScheme,
                    prefValueEncryptionScheme,
                )
            }.onSuccess { return@lazy it }
        }
        runCatching { KeyStore.getInstance(KeyStore.getDefaultType()).apply { load(null) } }
        EncryptedSharedPreferences.create(
            context,
            fileName,
            masterKey,
            prefKeyEncryptionScheme,
            prefValueEncryptionScheme,
        )
    }
}