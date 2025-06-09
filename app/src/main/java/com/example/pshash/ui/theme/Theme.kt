package com.example.pshash.ui.theme

import android.app.Activity
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
val Purple40 = Color(0xFF453A62)
//val Pink40 = Color(0xFF8F4E8B)

val LightColorScheme = darkColorScheme(
    primary = Purple80,
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
    primary = DarkPurple40,
    secondary = DarkPurpleGrey40,
    tertiary = DarkDarkPurpleGrey40,
    background = Color(0xFF170E17),
    surface = DarkPurple40,//Color(0xFF1A0E1A),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

val iconSize = 30.dp
val smallIconSize = 20.dp
val barHeight = 84.dp

@Composable
fun PshashTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}