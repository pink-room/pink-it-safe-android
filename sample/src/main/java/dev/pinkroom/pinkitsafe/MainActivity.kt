package dev.pinkroom.pinkitsafe

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
        if (biometrics.isAvailable()) {
            BiometricsScreen(biometrics, safeStorage)
        } else {
            Text(text = "Biometrics not available")
        }
    }
}

@Composable
private fun BiometricsScreen(
    biometrics: Biometrics,
    safeStorage: SafeStorage
) {
    var key by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }

    OutlinedTextField(
        value = key,
        onValueChange = { key = it },
        label = { Text(text = "Key") },
    )
    OutlinedTextField(
        value = value,
        onValueChange = { value = it },
        label = { Text(text = "Value") },
    )
    Row(modifier = Modifier.padding(top = 8.dp)) {
        Button(
            onClick = { biometrics.authenticate { safeStorage.save(key, value) } },
            content = { Text(text = "Save") },
        )
        Button(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = { biometrics.authenticate { value = safeStorage.get(key) } },
            content = { Text(text = "Get") },
        )
        Button(
            onClick = {
                biometrics.authenticate {
                    safeStorage.clear()
                    key = ""
                    value = ""
                }
            },
            content = { Text(text = "Clear") },
        )
    }
}

private fun Biometrics.authenticate(success: () -> Unit) =
    authenticate("Authenticate", success = success)