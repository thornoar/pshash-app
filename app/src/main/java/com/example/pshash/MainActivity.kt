package com.example.pshash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.indication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pshash.ui.theme.PshashTheme
import com.example.pshash.ui.theme.Typography
import com.example.pshash.ui.theme.barHeight
import com.example.pshash.ui.theme.iconSize
import com.example.pshash.ui.theme.smallIconSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

import com.example.pshash.*
import java.nio.channels.Selector

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PshashTheme { TopLevel() } }
    }
}

@Preview
@Composable
fun TopLevel (
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
) {
    val navController = rememberNavController()

    val navigationState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    val navigationScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        gesturesEnabled = false,
        drawerState = navigationState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.then(
                    if (navigationState.targetValue == DrawerValue.Open) Modifier.fillMaxSize() else Modifier
                ),
                drawerContainerColor = MaterialTheme.colorScheme.primary
            ) {
                DrawerContent(
                    onCloseDrawer = {
                        navigationScope.launch {
                            navigationState.apply {
                                if (isOpen) close()
                            }
                        }
                    },
                    navController = navController
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "generate",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable(
                route = "generate",
            ) {
                HomeContent(navigationScope, navigationState, navController)
            }
            composable(
                route = "info",
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = SlideDirection.Left
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = SlideDirection.Right
                    )
                }
            ) {
                InfoContent(navController)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionTopBar(
    title : String,
    onOpenDrawer : () -> Unit,
    navController : NavController
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
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(iconSize)
                    .clickable { onOpenDrawer() }
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Info",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .size(iconSize)
                    .clickable {
                        navController.navigate(route = "info")
                    }
            )
        }
    )
}

@Composable
fun DrawerContent (
    onCloseDrawer : () -> Unit,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, bottom = 14.dp, start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "available functions",
            fontSize = Typography.titleLarge.fontSize,
            fontWeight = Typography.titleLarge.fontWeight
        )

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close drawer",
            modifier = Modifier
                .size(iconSize)
                .clickable {
                    onCloseDrawer()
                }
        )
    }
    HorizontalDivider()

    @Composable
    fun DrawerItem (
        imageVector : ImageVector,
        function : String,
        route : String
    ) {
        NavigationDrawerItem(
            shape = RectangleShape,
            modifier = Modifier.height(65.dp),
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = MaterialTheme.colorScheme.secondary,
                selectedContainerColor = MaterialTheme.colorScheme.primary
            ),
            icon = {
                Icon(
                    imageVector = imageVector,
                    contentDescription = function,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .size(smallIconSize)
                )
            },
            label = {
                Text(
                    function,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 20.sp
                )
            },
            selected = false,//currentRoute == route,
            onClick = {
                onCloseDrawer()
                navController.navigate(route)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        DrawerItem(Icons.Default.Build, "generate password", "generate")
        DrawerItem(Icons.Outlined.Info, "read info", "info")
    }

}

@Composable
fun TextBox(
    text: MutableState<String>,
    default: String,
    textPadding: Dp,
    boxPadding: Dp,
    valid: Boolean,
    selected: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = boxPadding, end = boxPadding)
            .border(if (selected) 2.dp else 1.dp, if (valid) Color.Green else Color.Red, RoundedCornerShape(0.dp))
    ) {
        Text(
            text = text.value.ifEmpty { default },
            fontSize = 18.sp,
            modifier = Modifier
                .padding(top = textPadding, bottom = textPadding, start = textPadding)
                .alpha(if (text.value.isEmpty()) 0.5f else 1f)
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
            .height(50.dp)
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

@Composable
fun ConfigSelector(
    text: MutableState<String>,
    currentPoint: MutableIntState,
    boxPadding: Dp
) {
    SelectorTitle("choose configuration")

    HorizontalDivider()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
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
                    .height(40.dp)
            ) {
                Text(
                    text = item,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                )
            }
        }
    }

    HorizontalDivider()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BottomButton(
            onClick = { },
            text = "previous"
        )
        BottomButton(
            onClick = { currentPoint.intValue = 2 },
            text = "next"
        )
    }
}

@Composable
fun PublicSelector(
    text: MutableState<String>,
    currentPoint: MutableIntState,
    boxPadding: Dp
) {
    SelectorTitle("choose public key")
    HorizontalDivider()
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        BottomButton(
            onClick = { currentPoint.intValue = 1 },
            text = "previous"
        )
        BottomButton(
            onClick = { if (!text.value.isEmpty()) { currentPoint.intValue = 3 } },
            text = "next"
        )
    }
}

@Composable
fun HomeContent(
    navigationScope : CoroutineScope,
    navigationState : DrawerState,
    navController : NavController
) {
    val currentPoint = remember { mutableIntStateOf(1) }
    val generated = remember { mutableStateOf(false) }
    val password = remember { mutableStateOf("") }

    val config = remember { mutableStateOf("") }
    val public = remember { mutableStateOf("") }
    val choice = remember { mutableStateOf("") }
    val shuffle = remember { mutableStateOf("") }

    val validConfig = availableConfigKeywords.any { it == config.value }
    val validPublic = isValidPublicKey(public.value)
    val validChoice = isValidPrivateKey(choice.value)
    val validShuffle = isValidPrivateKey(shuffle.value)

    val textPadding = 14.dp
    val boxPadding = 16.dp

    Scaffold(
        topBar = {
            FunctionTopBar(
                title = "generate password",
                onOpenDrawer = {
                    navigationScope.launch {
                        navigationState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                },
                navController = navController
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
                    TextBox(config, "configuration keyword", textPadding, boxPadding, validConfig, currentPoint.intValue == 1)
                    TextBox(public, "public key", textPadding, boxPadding, validPublic, currentPoint.intValue == 2)
                    TextBox(choice, "choice private key", textPadding, boxPadding, validChoice, currentPoint.intValue == 3)
                    TextBox(shuffle, "shuffle private key", textPadding, boxPadding, validShuffle, currentPoint.intValue == 4)
                }

                HorizontalDivider()

                if (currentPoint.intValue == 1) ConfigSelector(config, currentPoint, boxPadding)
                else if (currentPoint.intValue == 2) PublicSelector(public, currentPoint, boxPadding)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoContent (
    navController : NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar (
                modifier = Modifier.heightIn(max = barHeight),
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(text = "program info", color = MaterialTheme.colorScheme.onPrimary)
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp)
                            .size(iconSize)
                            .clickable { navController.popBackStack() }
                    )
                }
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
                    "`pshash` is a pseudo-hash algorithm implemented in Haskell, JavaScript, and C/C++. It serves as a password manager by accepting three keys (one public and two private) and returning a pseudo-hash that can be used as a password. The program does not store the passwords anywhere, instead it generates them on the fly every time, which ensures a significant degree of security.\n" +
                    "\n" +
                    "Various password templates are supported, and the user is free to define their own. These templates can then be stored in a configuration file, one template per public key. This way, the user can produce different types of passwords for different public keys, and does not have to keep all the templates in their head.\n" +
                    "\n" +
                    "The algorithm was designed to withstand brute-forcing as well. For finer detail, please refer to the corresponding mathematical paper: documentation/main.pdf\n" +
                    "\n" +
                    "This repository contains the CLI version of the pshash algorithm, implemented in Haskell, the web version, written in JavaScript, and the standalone GUI version, written in C/C++. All versions implement the algorithm natively, with no foreign function interface."
                )
            }
        }
    }
}