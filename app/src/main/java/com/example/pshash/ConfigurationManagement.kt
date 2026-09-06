package com.example.pshash

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun GeneralConfigContent(
    inMenu: MutableState<Boolean>,
    inInfo: MutableState<Boolean>,
) {
    Scaffold(
        topBar = {
            FunctionTopBar(
                title = "generate password",
                leftIcon = Icons.Filled.Menu,
                leftDesc = "Menu",
                leftCallback = { inMenu.value = true },
                hasRight = true,
                rightIcon = Icons.Outlined.Info,
                rightDesc = "Info",
                rightCallback = { inInfo.value = true }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.secondary
        ) {
            Text("text", modifier = Modifier.padding(innerPadding))
        }
    }
}