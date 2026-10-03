package com.example.pshash

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pshash.ui.theme.boxPadding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Optional

private fun readTextFromUri(context: android.content.Context, uri: Uri): String {
    return context.contentResolver.openInputStream(uri)?.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            reader.readText()
        }
    } ?: "Unable to read file"
}

@Composable
fun GeneralConfigContent(
    inMenu: MutableState<Boolean>,
    inInfo: MutableState<Boolean>,
    currentScreen: MutableIntState,
    currentPoint: MutableIntState,
    public: MutableState<String>,
    patch: MutableState<String>,
    config: MutableState<String>,
    configEntries: SnapshotStateList<ConfigEntry>,
    presetConfig: MutableState<Optional<List<Pair<List<Char>, Int>>>>,
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            val text = readTextFromUri(context, uri)

            val parsedEntries = parseConfig(text)
            if (parsedEntries.isEmpty) {
                configEntries.add(ConfigEntry("asd", Optional.empty(), 0))
            } else {
                configEntries.clear()
                configEntries.addAll(parsedEntries.get())
                writeConfigFile(context, text)
            }
        }
    }

    Scaffold(
        topBar = {
            FunctionTopBar(
                title = "configurations",
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
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(boxPadding),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(start = boxPadding, end = boxPadding, bottom = boxPadding)
                        .fillMaxWidth()
                ) {
                    BoxedTextButton(
                        onClick = {
                            launcher.launch("*/*")
                        },
                        text = "pick file",
                        Modifier.weight(1f)
                    )

//                    BoxedTextButton(
//                        onClick = {},
//                        text = "add",
//                        Modifier.weight(1f)
//                    )
                }

                HorizontalDivider()

                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    itemsIndexed(configEntries) { ind, item ->
                        EntryContent(ind, currentScreen, currentPoint, public, patch, config,item, presetConfig)
                    }
                }
            }
        }
    }
}

@Composable
fun EntryContent(
    ind: Int,
    currentScreen: MutableIntState,
    currentPoint: MutableIntState,
    public: MutableState<String>,
    patch: MutableState<String>,
    config: MutableState<String>,
    configEntry: ConfigEntry,
    presetConfig: MutableState<Optional<List<Pair<List<Char>, Int>>>>,
) {
    PlainTextButton(
        onClick = {
            public.value = configEntry.pubkey
            patch.value = configEntry.patch.toString()
            if (configEntry.config.isPresent) {
                config.value = "(preset for ${configEntry.pubkey})"
                presetConfig.value = Optional.of(configEntry.config.get())
            }
            currentScreen.intValue = generateScreenId
            currentPoint.intValue = 4
        },
        text = configEntry.pubkey,
        modifier = Modifier
            .background(if (ind % 2 == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary)
            .height(50.dp)
            .fillMaxWidth(),
        fontSize = 18.sp
    )
}