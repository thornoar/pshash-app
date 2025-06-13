package com.example.pshash

import android.graphics.Paint.Align
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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

@Preview
@Composable
fun TopLevel (
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
) {
    val inMenu = remember { mutableStateOf(false) }
    val inInfo = remember { mutableStateOf(false) }
    val currentScreen = remember { mutableIntStateOf(generateScreenId) }

    // GeneratePassword states
    val currentPoint = remember { mutableIntStateOf(1) }
    val generated = remember { mutableStateOf(false) }
    val password = remember { mutableStateOf("") }
    val config = remember { mutableStateOf("") }
    val public = remember { mutableStateOf("") }
    val choice = remember { mutableStateOf("") }
    val shuffle = remember { mutableStateOf("") }

    if (inInfo.value) {
        InfoContent(inInfo)
    } else if (inMenu.value) {
        MenuContent(inMenu, inInfo, currentScreen)
    } else {
        if (currentScreen.intValue == generateScreenId) {
            GeneratePasswordContent(inMenu, inInfo, currentPoint, generated, password, config, public, choice, shuffle)
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
                title = "available functions",
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
            MenuButton("generate password", Icons.Rounded.Build, inMenu, currentScreen, generateScreenId)
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
                title = "available functions",
                leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
                leftDesc = "Menu",
                leftCallback = { inInfo.value = false },
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.tertiary
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(start = 28.dp, end = 28.dp)
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
                        "`pshash` is a pseudo-hash algorithm implemented in Haskell, JavaScript, C/C++ " +
                        "and Kotlin. It serves as a password manager by accepting a source configuration " +
                        "as well as three keys (one public and two private), returning a pseudo-hash " +
                        "that can be used as a password. The program does not store the passwords " +
                        "anywhere, instead it generates them on the fly every time, which ensures " +
                        "a significant degree of security.\n" +
                        "\n" +
                        "Various password templates are supported, and the user is free to define " +
                        "their own. These templates can then be stored in a configuration file, " +
                        "one template per public key. This way, the user can produce different types of " +
                        "passwords for different public keys, and does not have to keep all the " +
                        "templates in their head.\n" +
                        "\n" +
                        "The algorithm was designed to withstand brute-forcing as well. For finer " +
                        "detail, please refer to the corresponding mathematical paper found on " +
                        "the thornoar/pshash GitHub project under documentation/main.pdf\n" +
                        "\n" +
                        "This is the Android app version of the algorithm. There is also a web version " +
                        "available at thornoar.github.io/pshash/web/app, as well as a CLI version " +
                        "available in the Nix package manager or on the AUR. One may also build it from " +
                        "source at github.com/thornoar/pshash."
                )
            }
        }
    }
}

@Composable
fun GeneratePasswordContent(
    inMenu: MutableState<Boolean>,
    inInfo: MutableState<Boolean>,
    currentPoint: MutableIntState,
    generated: MutableState<Boolean>,
    password: MutableState<String>,
    config: MutableState<String>,
    public: MutableState<String>,
    choice: MutableState<String>,
    shuffle: MutableState<String>,
) {
    val validConfig = availableConfigKeywords.any { it == config.value }
    val validPublic = isValidPublicKey(public.value)
    val validChoice = isValidPrivateKey(choice.value)
    val validShuffle = isValidPrivateKey(shuffle.value)

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
                    TextBox(displayConfiguration(config.value), false, "source configuration", textPadding, boxPadding, validConfig, currentPoint.intValue == 1)
                    TextBox(public.value, false, "public key", textPadding, boxPadding, validPublic, currentPoint.intValue == 2)
                    TextBox(choice.value, true, "choice private key", textPadding, boxPadding, validChoice, currentPoint.intValue == 3)
                    TextBox(shuffle.value, true, "shuffle private key", textPadding, boxPadding, validShuffle, currentPoint.intValue == 4)
                }

                HorizontalDivider()

                if (currentPoint.intValue == 1) ConfigSelector(config, currentPoint)
                else if (currentPoint.intValue == 2) PublicSelector(public, currentPoint)
                else if (currentPoint.intValue == 3) PrivateSelector("enter choice private key", choice, currentPoint, nextVal = 4, prevVal = 2)
                else if (currentPoint.intValue == 4) PrivateSelector("enter shuffle private key", shuffle, currentPoint, nextVal = 5, prevVal = 3)
                else {
                    PasswordGenerator(
                        ready = validConfig && validPublic && validChoice && validShuffle,
                        password = password,
                        generated = generated,
                        config = config.value,
                        public = public.value,
                        choice = choice.value,
                        shuffle = shuffle.value,
                        currentPoint = currentPoint
                    )
                }
            }
        }
    }
}

@Composable
fun ConfigSelector(
    text: MutableState<String>,
    currentPoint: MutableIntState,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        SelectorTitle("choose configuration")
        HorizontalDivider()
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .background(MaterialTheme.colorScheme.tertiary)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(MaterialTheme.colorScheme.tertiary),
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
                        fontSize = 20.sp,
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
        HorizontalDivider()
        BottomRow(currentPoint, nextVal = 2, prevVal = 1)
    }
}

@Composable
fun PublicSelector(
    text: MutableState<String>,
    currentPoint: MutableIntState,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SelectorTitle("enter public key")
        HorizontalDivider()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        ) {
            val keyModifier = Modifier
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
            LetterRow("1234567890".toList(), text, keyModifier)
            LetterRow("qwertyuiop".toList(), text, keyModifier)
            LetterRow("asdfghjkl".toList(), text, keyModifier)
            LetterRow("zxcvbnm.-".toList(), text, keyModifier)
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
            ) {
                KeyboardKey("delete", keyModifier, { if (!text.value.isEmpty()) text.value = text.value.dropLast(1) })
            }
        }
        HorizontalDivider()
        BottomRow(currentPoint, nextVal = 3, prevVal = 1)
    }
}

@Composable
fun PrivateSelector(
    title: String,
    text: MutableState<String>,
    currentPoint: MutableIntState,
    nextVal: Int,
    prevVal: Int
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SelectorTitle(title)
        HorizontalDivider()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        ) {
            val keyModifier = Modifier
                .padding(
                    start = 30.dp,
                    end = 30.dp,
                    top = 20.dp,
                    bottom = 20.dp
                )
            LetterRow("789".toList(), text, keyModifier)
            LetterRow("456".toList(), text, keyModifier)
            LetterRow("123".toList(), text, keyModifier)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
            ) {
                KeyboardKey("-", keyModifier, { text.value = "${text.value}-" })
                KeyboardKey("delete", keyModifier, { if (!text.value.isEmpty()) text.value = text.value.dropLast(1) })
            }
        }
        HorizontalDivider()
        BottomRow(currentPoint, nextVal = nextVal, prevVal = prevVal)
    }
}

@Composable
fun PasswordGenerator(
    ready: Boolean,
    password: MutableState<String>,
    generated: MutableState<Boolean>,
    config: String,
    public: String,
    choice: String,
    shuffle: String,
    currentPoint: MutableIntState,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SelectorTitle(if (ready) "password generated!" else "...please give valid values")
        HorizontalDivider()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
        ) {
            if (ready) {
                password.value = getPassword(config, public, choice, shuffle)
                generated.value = true
                val clipboardManager = LocalClipboardManager.current
                val copied = remember { mutableStateOf(false) }
                Text(
                    text = if (copied.value) "copied!" else "copy to clipboard",
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .clickable {
                            clipboardManager.setText(AnnotatedString(password.value))
                            copied.value = true
                        }
                        .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(cornerRadius))
                        .padding(all = 14.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(200.dp)
                )
            }
        }
        HorizontalDivider()
        BottomRow(currentPoint, nextVal = 5, prevVal = 4)
    }
}

@Composable
fun LetterRow(
    letters: List<Char>,
    text: MutableState<String>,
    keyModifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        for (letter in letters) {
            KeyboardKey("$letter", keyModifier, { text.value = "${text.value}$letter" })
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
        fontSize = 20.sp,
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
    textPadding: Dp,
    boxPadding: Dp,
    valid: Boolean,
    selected: Boolean
) {
    val realText : String = if (text.isEmpty()) {
        default
    } else if (conceal) {
        "*".repeat(text.length)
    } else {
        text
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = boxPadding, end = boxPadding)
            .border(if (selected) 1.dp else 1.dp, if (valid) Color.Green else Color.Red, RoundedCornerShape(cornerRadius))
            .background(if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            text = realText,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(top = textPadding, bottom = textPadding, start = textPadding)
                .alpha(if (text.isEmpty()) 0.5f else 1f)
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
            .width(200.dp)
    ) {
        Text(
            fontSize = 22.sp,
            fontFamily = FontFamily.Monospace,
            color = MaterialTheme.colorScheme.onPrimary,
            text = text,
        )
    }
}

@Composable
fun BottomRow(
    currentPoint: MutableIntState,
    nextVal: Int,
    prevVal: Int
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BottomButton(
            onClick = { currentPoint.intValue = prevVal },
            text = "back"
        )
        BottomButton(
            onClick = { currentPoint.intValue = nextVal },
            text = "next"
        )
    }
}

@Composable
fun SelectorTitle(
    text: String
) {
    Text(
        fontSize = 22.sp,
        fontFamily = FontFamily.Monospace,
        color = MaterialTheme.colorScheme.onPrimary,
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .wrapContentHeight(align = Alignment.CenterVertically)
    )
}


//    val navController = rememberNavController()
//    val navigationState = rememberDrawerState(
//        initialValue = DrawerValue.Closed
//    )
//    val navigationScope = rememberCoroutineScope()
//
//    ModalNavigationDrawer(
//        gesturesEnabled = false,
//        drawerState = navigationState,
//        drawerContent = {
//            ModalDrawerSheet(
//                modifier = Modifier.then(
//                    if (navigationState.targetValue == DrawerValue.Open) Modifier.fillMaxSize() else Modifier
//                ),
//                drawerContainerColor = MaterialTheme.colorScheme.primary
//            ) {
//                DrawerContent(
//                    onCloseDrawer = {
//                        navigationScope.launch {
//                            navigationState.apply {
//                                if (isOpen) close()
//                            }
//                        }
//                    },
//                    navController = navController
//                )
//            }
//        }
//    ) {
//        NavHost(
//            navController = navController,
//            startDestination = "generate",
//            enterTransition = { EnterTransition.None },
//            exitTransition = { ExitTransition.None }
//        ) {
//            composable(
//                route = "generate",
//            ) {
//                GeneratePasswordContent(navigationScope, navigationState, navController)
//            }
//            composable(
//                route = "info",
//                enterTransition = {
//                    slideIntoContainer(
//                        animationSpec = tween(300, easing = EaseOut),
//                        towards = SlideDirection.Left
//                    )
//                },
//                exitTransition = {
//                    slideOutOfContainer(
//                        animationSpec = tween(300, easing = EaseOut),
//                        towards = SlideDirection.Right
//                    )
//                }
//            ) {
//                InfoContent(navController)
//            }
//        }
//    }

//@Composable
//fun DrawerContent (
//    onCloseDrawer : () -> Unit,
//    navController: NavController
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 14.dp, bottom = 14.dp, start = 16.dp, end = 16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            "available functions",
//            fontSize = Typography.titleLarge.fontSize,
//            fontWeight = Typography.titleLarge.fontWeight
//        )
//
//        Icon(
//            imageVector = Icons.Default.Close,
//            contentDescription = "Close drawer",
//            modifier = Modifier
//                .size(iconSize)
//                .clickable {
//                    onCloseDrawer()
//                }
//        )
//    }
//    HorizontalDivider()
//
//    @Composable
//    fun DrawerItem (
//        imageVector : ImageVector,
//        function : String,
//        route : String
//    ) {
//        NavigationDrawerItem(
//            shape = RectangleShape,
//            modifier = Modifier.height(65.dp),
//            colors = NavigationDrawerItemDefaults.colors(
//                unselectedContainerColor = MaterialTheme.colorScheme.secondary,
//                selectedContainerColor = MaterialTheme.colorScheme.primary
//            ),
//            icon = {
//                Icon(
//                    imageVector = imageVector,
//                    contentDescription = function,
//                    tint = MaterialTheme.colorScheme.onPrimary,
//                    modifier = Modifier
//                        .padding(start = 16.dp, end = 8.dp)
//                        .size(smallIconSize)
//                )
//            },
//            label = {
//                Text(
//                    function,
//                    color = MaterialTheme.colorScheme.onPrimary,
//                    fontSize = 20.sp
//                )
//            },
//            selected = false,//currentRoute == route,
//            onClick = {
//                onCloseDrawer()
//                navController.navigate(route)
//            }
//        )
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.secondary),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.Start
//    ) {
//        Spacer(modifier = Modifier.height(10.dp))
//        DrawerItem(Icons.Default.Build, "generate password", "generate")
//        DrawerItem(Icons.Outlined.Info, "read info", "info")
//    }
//
//}
