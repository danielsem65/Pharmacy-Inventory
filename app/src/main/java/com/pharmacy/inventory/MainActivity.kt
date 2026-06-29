package com.pharmacy.inventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pharmacy.inventory.ui.MainScreen
import com.pharmacy.inventory.ui.MedicineViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val colorScheme = lightColorScheme(
                primary = Color(0xFF1565C0),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFD1E4FF),
                secondary = Color(0xFF546E7A),
                surface = Color(0xFFFEFBFF),
                onSurface = Color(0xFF1B1B1F),
                onSurfaceVariant = Color(0xFF44474E)
            )
            MaterialTheme(colorScheme = colorScheme) {
                val viewModel: MedicineViewModel = viewModel()
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
