package net.chasmine.ninjalauncher

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import net.chasmine.ninjalauncher.ui.screen.LauncherScreen
import net.chasmine.ninjalauncher.ui.theme.NINJALauncherTheme
import java.util.concurrent.Executor

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NINJALauncherTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val context = LocalContext.current
                    val executor = ContextCompat.getMainExecutor(context)
                    val biometricPrompt = createBiometricPrompt(executor) { success ->
                        if (success) {
                            // NINJAモードの認証成功処理
                        }
                    }

                    LauncherScreen(
                        onNinjaModeToggle = {
                            showBiometricPrompt(biometricPrompt)
                        }
                    )
                }
            }
        }
    }

    private fun createBiometricPrompt(executor: Executor, onResult: (Boolean) -> Unit): BiometricPrompt {
        return BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onResult(false)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onResult(true)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onResult(false)
            }
        })
    }

    private fun showBiometricPrompt(biometricPrompt: BiometricPrompt) {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("NINJAモード認証")
            .setSubtitle("NINJAモードに切り替えるには認証が必要です")
            .setNegativeButtonText("キャンセル")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
