package com.example.pshash

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import github.mahdiasd.composefilepicker.screens.PickerDialog
import github.mahdiasd.composefilepicker.utils.PickerType
import java.util.Optional

@Composable
fun GeneralConfigContent(
    inMenu: MutableState<Boolean>,
    inInfo: MutableState<Boolean>,
    configEntries: SnapshotStateList<ConfigEntry>
) {
    val inPicker = remember { mutableStateOf(false) }
//    val pickerTypes = listOf(PickerType.Storage).toImmutableList()

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
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.tertiary)
                        .fillMaxWidth()
                ) {
                    PlainTextButton(
                        onClick = { inPicker.value = true },
                        text = "pick"
                    )
                    PlainTextButton(
                        onClick = { configEntries.add(ConfigEntry("asd", Optional.empty(), 55)) },
                        text = "add"
                    )
                }

                if (inPicker.value) {
//                    PickerDialog(
//                        types = pickerTypes,
//                        pickerConfig = PickerConfig(maxSelection = 5),
//                        onDismiss = { showDialog = false },
//                        selected = { files ->
//                            onFilesSelected(files)
//                            showDialog = false
//                        }
//                    )
                } else {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        itemsIndexed(configEntries) { _, item ->
                            EntryContent(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EntryContent(
    entry: ConfigEntry
) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Text(entry.pubkey)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(entry.config.toString(), modifier = Modifier.weight(1f))
            Text(entry.patch.toString(), modifier = Modifier.weight(1f))
        }
    }
}