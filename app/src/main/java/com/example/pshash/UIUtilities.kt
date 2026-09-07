package com.example.pshash

import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlainTextButton(
    onClick: () -> Unit,
    text: String
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .height(60.dp)
    ) {
        Text(
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onPrimary,
            text = text,
        )
    }
}
