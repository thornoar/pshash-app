package com.example.pshash.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

val Purple80 = Color(0xFFC7ADF3)
val PurpleGrey80 = Color(0xFFA190BD)
val Pink80 = Color(0xFF744D7E)

//val Purple120 = Color(0xFF6650a4)
//val PurpleGrey120 = Color(0xFF625b71)
//val Pink120 = Color(0xFF7D5260)

val DarkPurple40 = Color(0xFF312649)
val DarkPurpleGrey40 = Color(0xFF1F1931)
val DarkDarkPurpleGrey40 = Color(0xFF1B1728)

val LightColorScheme = darkColorScheme(
    primary = DarkPurple40,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFFF1E0F1),
    surface = Color(0xFFF2ECF6),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

val DarkColorScheme = lightColorScheme(
    primary = Color.Black,
    secondary = Color.Black,
//    tertiary = Color.Black,
    tertiary = Color(0xFF0E0E12),
//    tertiary = Color(0xFF1E1E24),
    background = Color.Black,
    surface = Color.Black,//Color(0xFF1A0E1A),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

val iconSize = 30.dp
val smallIconSize = 24.dp
val barHeight = 84.dp

val textPadding = 14.dp
val boxPadding = 16.dp

val cornerRadius = 5.dp

@Composable
fun PshashTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}