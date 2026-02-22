package com.example.pshash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pshash.ui.theme.PshashTheme
import com.example.pshash.ui.theme.barHeight
import com.example.pshash.ui.theme.iconSize

import com.example.pshash.ui.theme.PurpleGrey80
import com.example.pshash.ui.theme.boxPadding
import com.example.pshash.ui.theme.cornerRadius
import com.example.pshash.ui.theme.smallIconSize
import com.example.pshash.ui.theme.textPadding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PshashTheme { TopLevel() } }
    }
}

const val generateScreenId : Int = 0
const val manageConfigId : Int = 0

@Preview
@Composable
fun TopLevel () {
    val inMenu = remember { mutableStateOf(false) }
    val inInfo = remember { mutableStateOf(false) }
    val currentScreen = remember { mutableIntStateOf(generateScreenId) }

    // GeneratePassword states
    val currentPoint = remember { mutableIntStateOf(1) }
    val config = remember { mutableStateOf("") }
    val public = remember { mutableStateOf("") }
    val patch = remember { mutableStateOf("") }
    val choice = remember { mutableStateOf("") }
    val shuffle = remember { mutableStateOf("") }
    val inMnemonic = remember { mutableStateOf(true) }

    if (inInfo.value) {
        InfoContent(inInfo)
    } else if (inMenu.value) {
        MenuContent(inMenu, inInfo, currentScreen)
    } else {
        if (currentScreen.intValue == generateScreenId) {
            GeneratePasswordContent(inMenu, inInfo, currentPoint, config, public, patch, choice, shuffle, inMnemonic)
        } else if (currentScreen.intValue == manageConfigId) {
            GeneratePasswordContent(inMenu, inInfo, currentPoint, config, public, patch, choice, shuffle, inMnemonic)
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: ImageVector,
    inMenu: MutableState<Boolean>,
    currentScreen: MutableIntState,
    setTo: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                currentScreen.intValue = setTo
                inMenu.value = false
            }
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = boxPadding + 10.dp)
        )
        Icon(
            imageVector = icon,
            contentDescription = "Icon",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(end = boxPadding + 10.dp)
                .size(smallIconSize)
        )
    }
}

@Composable
fun MenuContent(
    inMenu: MutableState<Boolean>,
    inInfo: MutableState<Boolean>,
    currentScreen: MutableIntState
) {
    Scaffold(
        topBar = {
            FunctionTopBar(
                title = "capabilities",
                leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
                leftDesc = "Menu",
                leftCallback = { inMenu.value = false },
                hasRight = true,
                rightIcon = Icons.Outlined.Info,
                rightDesc = "Info",
                rightCallback = { inInfo.value = true }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            HorizontalDivider()
            MenuButton("generate password", Icons.Rounded.Lock, inMenu, currentScreen, generateScreenId)
//            HorizontalDivider()
//            MenuButton("manage configurations", Icons.Rounded.Settings, inMenu, currentScreen, manageConfigId)
        }
    }
}

@Composable
fun InfoContent (
    inInfo: MutableState<Boolean>,
) {
    Scaffold(
        topBar = {
            FunctionTopBar(
                title = "info page",
                leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
                leftDesc = "Menu",
                leftCallback = { inInfo.value = false },
            )
        }
    ) { innerPadding ->
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.tertiary
//        ) {
//        HorizontalDivider()
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 14.dp, end = 14.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(28.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_white),
                contentDescription = "The logo",
            )
            Spacer(modifier = Modifier.height(25.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text =
                    "`pshash` is a pseudo-hash password generation algorithm. " +
                    "It serves as a password manager by accepting a source configuration " +
                    "as well as three keys (one public and two private), returning a pseudo-hash " +
                    "that can be used as a password. The program does not store the passwords " +
                    "anywhere, instead it generates them on the fly every time, which ensures " +
                    "a significant degree of security.\n" +
                    "\n" +
                    "This is the Android app version of the algorithm. There is also a web version " +
                    "available at thornoar.github.io/pshash-web/app, as well as a CLI version " +
                    "available in the Nix package manager or on the AUR. One may also build it from " +
                    "source at github.com/thornoar/pshash.\n" +
                    "\n" +
                    "To generate a password, go to the \"generate password\" page and provide the " +
                    "specified inputs. The \"source configuration\" defines the character composition " +
                    "of the password. The \"public key\" is an alphanumeric string that identifies " +
                    "the destination of the password. The \"choice\" and \"shuffle\" keys are two " +
                    "large numbers. They are constant across all applications of `pshash` and should " +
                    "be kept secret. For easier input, `pshash` supports arithmetic expressions " +
                    "and mnemonic incantations as input methods.\n" +
                    "\n" +
                    "The algorithm was designed to withstand brute-forcing. For finer " +
                    "detail, please refer to the corresponding mathematical paper found on " +
                    "the thornoar/pshash GitHub project under paper/main.pdf.\n" +
                    "\n" +
                    "The current version is 1.5-alpha."
            )
        }
//        }
    }
}

@Composable
fun LetterRow(
    letters: List<String>,
    text: MutableState<String>,
    modifier: Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for (letter in letters) {
            KeyboardKey(
                letter,
                modifier,
                {
                    text.value = "${text.value}$letter"
                }
            )
        }
    }
}

@Composable
fun KeyboardKey(
    key: String,
    modifier: Modifier,
    onPress: () -> Unit
) {
    var isKeyPressed by remember { mutableStateOf(false) }
    Text(
        text = key,
        fontSize = 19.sp,
        fontFamily = FontFamily.Monospace,
        color = if (isKeyPressed) PurpleGrey80 else MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    isKeyPressed = true
                    val success = tryAwaitRelease()
                    if (success) {
                        isKeyPressed = false
                        onPress()
                    } else {
                        isKeyPressed = false
                    }
                })
            }
            .border(1.dp, if (isKeyPressed) PurpleGrey80 else MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(cornerRadius))
            .then(modifier)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionTopBar(
    title : String,
    leftIcon: ImageVector,
    leftDesc: String,
    leftCallback: () -> Unit,
    hasRight: Boolean = false,
    rightIcon: ImageVector = Icons.Outlined.Info,
    rightDesc: String = "",
    rightCallback: () -> Unit = {},
) {
    CenterAlignedTopAppBar (
        modifier = Modifier.heightIn(max = barHeight),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Text(text = title, color = MaterialTheme.colorScheme.onPrimary)
        },
        navigationIcon = {
            Icon(
                imageVector = leftIcon,
                contentDescription = leftDesc,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(iconSize)
                    .clickable { leftCallback() }
            )
        },
        actions = if (hasRight) {
            {
                Icon(
                    imageVector = rightIcon,
                    contentDescription = rightDesc,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)
                        .size(iconSize)
                        .clickable {
                            rightCallback()
                        }
                )
            }
        } else { {} }
    )
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