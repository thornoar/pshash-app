package com.example.pshash

import androidx.annotation.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pshash.ui.theme.cornerRadius
import com.example.pshash.ui.theme.textPadding
import org.w3c.dom.Text

@Composable
fun PlainTextButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier,
    fontSize: TextUnit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .clickable { onClick() }
    ) {
        Text(
            fontSize = fontSize,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onPrimary,
            text = text,
        )
    }
//    TextButton(
//        onClick = onClick,
//        modifier = Modifier
//            .height(60.dp)
//    ) {
//    }
}

@Composable
fun BoxedTextButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)
            .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.secondary)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(top = textPadding, bottom = textPadding, start = textPadding)
                .alpha(if (text.isEmpty()) 0.5f else 1f)
        )
    }
}

@Composable
fun TextBox(
    text: String,
    conceal: Boolean,
    default: String,
    valid: Boolean,
    currentPoint: MutableIntState,
    setTo: Int,
    modifier: Modifier
) {
    val realText : String = if (text.isEmpty()) {
        default
    } else if (conceal) {
        "(" + text.length.toString() + " letters)"
    } else {
        text
    }
    val selected : Boolean = currentPoint.intValue == setTo
    Box(
        modifier = Modifier
            .then(modifier)
            .border(1.dp, if (valid) Color.Green else if (selected) Color.Yellow else Color.Red, RoundedCornerShape(cornerRadius))
            .background(if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary)
            .clickable { currentPoint.intValue = setTo }
    ) {
        Text(
            text = realText,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(top = textPadding, bottom = textPadding, start = textPadding)
                .alpha(if (text.isEmpty()) 0.5f else 1f)
        )
    }
}

