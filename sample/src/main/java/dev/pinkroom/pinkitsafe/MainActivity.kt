package dev.pinkroom.pinkitsafe

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.pinkroom.pinkitsafe.ui.theme.PinkItSafeTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PinkItSafeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
private fun MainScreen() {
    val context = LocalContext.current
    val safeStorage = SafeStorage(context, authenticationRequired = true)
    val biometrics = Biometrics(context)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var counter by remember { mutableStateOf(0) }

        Text(text = counter.toString())

        Button(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            onClick = {
                biometrics.authenticate {
                    counter = (safeStorage.get("counter") ?: 0) + 1
                    safeStorage.save("counter", counter)
                }
            }
        ) {
            Text(text = "Increment")
        }

        Button(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            onClick = {
                biometrics.authenticate {
                    safeStorage.clear()
                    counter = safeStorage.get("counter") ?: 0
                }
            }
        ) {
            Text(text = "Clear")
        }
    }
}

private fun Biometrics.authenticate(success: () -> Unit) =
    authenticate("Authenticate", success = success)