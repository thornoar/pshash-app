package com.example.pshash

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.pshash.ui.theme.boxPadding
import com.example.pshash.ui.theme.cornerRadius
import kotlin.math.max
import kotlin.math.min
import kotlin.text.ifEmpty

@Composable
fun GeneratePasswordContent(
    inMenu: MutableState<Boolean>,
    inInfo: MutableState<Boolean>,
    currentPoint: MutableIntState,
    config: MutableState<String>,
    public: MutableState<String>,
    patch: MutableState<String>,
    choice: MutableState<String>,
    shuffle: MutableState<String>,
    inMnemonic: MutableState<Boolean>,
) {
    val validConfig = availableConfigKeywords.any { it == config.value }
    val validPublic = isValidPublicKey(public.value)
    val validPatch = patch.value.isDigitsOnly()
    val validChoice = isValidPrivateKey(choice.value, inMnemonic.value)
    val validShuffle = isValidPrivateKey(shuffle.value, inMnemonic.value)
    val ready = validConfig && validPublic && validChoice && validShuffle

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
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                ) {
                    val defaultModifier = Modifier
                        .fillMaxWidth()
                        .padding(start = boxPadding, end = boxPadding)
                    TextBox(displayConfiguration(config.value), false, "source configuration", validConfig, currentPoint, 1, defaultModifier)
                    Row(
                        modifier = defaultModifier,
                        horizontalArrangement = Arrangement.spacedBy(boxPadding)
                    ) {
                        TextBox(public.value, false, "public key", validPublic, currentPoint, 2, Modifier.weight(5f))
                        TextBox(patch.value, false, "0", validPatch, currentPoint, 3, Modifier.weight(1f))
                    }
                    TextBox(choice.value, true, "choice private key", validChoice, currentPoint, 4, defaultModifier)
                    TextBox(shuffle.value, true, "shuffle private key", validShuffle, currentPoint, 5, defaultModifier)
                }

                val modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.tertiary)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    HorizontalDivider()
                    SelectorTitle(
                        when (currentPoint.intValue) {
                            1 -> "source configuration"
                            2 -> "public key"
                            3 -> "patch public key"
                            4 -> "choice private key"
                            5 -> "shuffle private key"
                            else -> if (ready) "password generated!" else "...em, invalid values"
                        }
                    )
                    HorizontalDivider()

                    when (currentPoint.intValue) {
                        1 -> ConfigSelector(config, modifier)
                        2 -> PublicSelector(public, modifier)
                        3 -> PatchSelector(patch, modifier)
                        4 -> PrivateSelector(choice, inMnemonic, modifier)
                        5 -> PrivateSelector(shuffle, inMnemonic, modifier)
                        else -> PasswordGenerator(
                            ready = ready,
                            config = config.value,
                            public = public.value,
                            patch = patch.value.ifEmpty { "0" },
                            choice = choice.value,
                            shuffle = shuffle.value,
                            mnemonic = inMnemonic.value,
                            modifier = modifier
                        )
                    }

                    HorizontalDivider()
                    BottomRow(
                        currentPoint = currentPoint,
                        nextVal = min(currentPoint.intValue + 1, 6),
                        prevVal = max(currentPoint.intValue - 1, 1),
                        config = config,
                        public = public,
                        patch = patch,
                        choice = choice,
                        shuffle = shuffle
                    )
                }

            }
        }
    }
}

@Composable
fun ConfigSelector(
    text: MutableState<String>,
    modifier: Modifier
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(15.dp)
            .background(MaterialTheme.colorScheme.tertiary)
    )
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        itemsIndexed(availableConfigKeywords.asList()) { index, item ->
            TextButton(
                onClick = { text.value = item },
                modifier = Modifier
                    .background(if (item == text.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary)
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = displayConfiguration(item),
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                )
            }
        }
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(15.dp)
            .background(MaterialTheme.colorScheme.tertiary)
    )
}

@Composable
fun PublicSelector(
    text: MutableState<String>,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        val keyModifier = Modifier
            .padding(
                vertical = 10.dp,
                horizontal = 9.dp
            )
        LetterRow(separate("1234567890"), text, keyModifier)
        LetterRow(separate("qwertyuiop"), text, keyModifier)
        LetterRow(separate("asdfghjkl"), text, keyModifier)
        LetterRow(separate("zxcvbnm.-"), text, keyModifier)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            KeyboardKey("clear", keyModifier, { text.value = "" })
            KeyboardKey("⌫", Modifier.padding(vertical = 10.dp, horizontal = 26.dp), { if (!text.value.isEmpty()) text.value = text.value.dropLast(1) })
        }
    }
}

@Composable
fun PatchSelector(
    text: MutableState<String>,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        val keyModifier = Modifier
            .padding(
                vertical = 16.dp,
                horizontal = 16.dp
            )
            .width(25.dp)
        val horizontalArrangement = Arrangement.SpaceEvenly
        @Composable
        fun RegularKey(
            key: String
        ) {
            KeyboardKey(key, keyModifier, { text.value = "${text.value}$key" })
        }
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            RegularKey("7")
            RegularKey("8")
            RegularKey("9")
            RegularKey("0")
        }
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            RegularKey("4")
            RegularKey("5")
            RegularKey("6")
            KeyboardKey("⌫", keyModifier, { text.value = text.value.dropLast(1) })
        }
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            RegularKey("1")
            RegularKey("2")
            RegularKey("3")
            KeyboardKey("cl", keyModifier, { text.value = "" })
        }
    }
}

@Composable
fun PrivateSelector(
    text: MutableState<String>,
    inMnemonic: MutableState<Boolean>,
    modifier: Modifier
) {
    if (inMnemonic.value) {
        PrivateMnemonicSelector(text, inMnemonic, modifier)
    } else {
        PrivateArithmeticSelector(text, inMnemonic, modifier)
    }
}

@Composable
fun PrivateMnemonicSelector(
    text: MutableState<String>,
    inMnemonic: MutableState<Boolean>,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        val keyModifier = Modifier
            .padding(
                vertical = 10.dp,
                horizontal = 9.dp
            )
        LetterRow(separate("qwertyuio"), text, keyModifier)
        LetterRow(separate("asdfghjkp"), text, keyModifier)
        LetterRow(separate("zxcvbnml"), text, keyModifier)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            KeyboardKey("clear", keyModifier, { text.value = "" })
            KeyboardKey("⌫", Modifier.padding(vertical = 10.dp, horizontal = 26.dp), { if (!text.value.isEmpty()) text.value = text.value.dropLast(1) })
            KeyboardKey("arith", keyModifier, {
                text.value = ""
                inMnemonic.value = false
            })
        }
    }
}

@Composable
fun PrivateArithmeticSelector(
    text: MutableState<String>,
    inMnemonic: MutableState<Boolean>,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        val keyModifier = Modifier
            .padding(
                vertical = 16.dp,
                horizontal = 16.dp
            )
            .width(25.dp)
        val horizontalArrangement = Arrangement.SpaceEvenly
        @Composable
        fun RegularKey(
            key: String
        ) {
            KeyboardKey(key, keyModifier, { text.value = "${text.value}$key" })
        }
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            RegularKey("7")
            RegularKey("8")
            RegularKey("9")
            RegularKey("0")
        }
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            RegularKey("4")
            RegularKey("5")
            RegularKey("6")
            KeyboardKey("⌫", keyModifier, { text.value = text.value.dropLast(1) })
        }
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            RegularKey("1")
            RegularKey("2")
            RegularKey("3")
            KeyboardKey("cl", keyModifier, { text.value = "" })
        }
        Row(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            RegularKey("+")
            RegularKey("*")
            RegularKey("^")
            KeyboardKey("mn", keyModifier, {
                text.value = ""
                inMnemonic.value = true
            })
        }
    }
}

@Composable
fun PasswordGenerator(
    ready: Boolean,
    config: String,
    public: String,
    patch: String,
    choice: String,
    shuffle: String,
    mnemonic: Boolean,
    modifier: Modifier
) {
    if (ready) {
        val password = getPassword(config, public, patch, choice, shuffle, mnemonic)
        val clipboardManager = LocalClipboardManager.current
        val copied = remember { mutableStateOf(false) }
        val show = remember { mutableStateOf(false) }
        SelectorTitle(
            if (show.value) password else "(click here to show)",
            modifier = Modifier
                .clickable {
                    show.value = !show.value
                }
        )
        HorizontalDivider()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            Text(
                text = if (copied.value) "copied!" else "copy to clipboard",
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .clickable {
                        clipboardManager.setText(AnnotatedString(password))
                        copied.value = true
                    }
                    .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(cornerRadius))
                    .padding(all = 14.dp)
            )
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(200.dp)
            )
        }
    }

}
@Composable
fun BottomRow(
    currentPoint: MutableIntState,
    nextVal: Int,
    prevVal: Int,
    config: MutableState<String>,
    public: MutableState<String>,
    patch: MutableState<String>,
    choice: MutableState<String>,
    shuffle: MutableState<String>,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BottomButton(
            onClick = { currentPoint.intValue = prevVal },
            text = "back"
        )
        BottomButton(
            onClick = {
                currentPoint.intValue = 1
                config.value = ""
                public.value = ""
                patch.value = ""
                choice.value = ""
                shuffle.value = ""
            },
            text = "over"
        )
        BottomButton(
            onClick = { currentPoint.intValue = 6 },
            text = "last"
        )
        BottomButton(
            onClick = { currentPoint.intValue = nextVal },
            text = "next"
        )
    }
}

@Composable
fun BottomButton(
    onClick: () -> Unit,
    text: String
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .height(60.dp)
//            .width(200.dp)
    ) {
        Text(
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onPrimary,
            text = text,
        )
    }
}

@Composable
fun SelectorTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        fontSize = 20.sp,
        fontFamily = FontFamily.Monospace,
        color = MaterialTheme.colorScheme.onPrimary,
        text = text,
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .height(50.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .wrapContentHeight(align = Alignment.CenterVertically)
    )
}
