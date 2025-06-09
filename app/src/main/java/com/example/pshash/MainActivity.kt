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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
fun HomeContent(
    navigationScope : CoroutineScope,
    navigationState : DrawerState,
    navController : NavController
) {
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
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .background(MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("text1")
                    Text("text2")
                }

                Text(
                    "text3",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.tertiary)
                )


//                var public by remember { mutableStateOf("") }
//                var choice by remember { mutableStateOf("") }
//                var shuffle by remember { mutableStateOf("") }
//                OutlinedTextField(
//                    modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp),
//                    value = public,
//                    onValueChange = { public = it },
//                    label = { Text("public key") },
//                    singleLine = true
//                )
//                OutlinedTextField(
//                    modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp),
//                    value = choice,
//                    onValueChange = { choice = it },
//                    label = { Text("choice key") },
//                    singleLine = true
//                )
//                OutlinedTextField(
//                    modifier = Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp),
//                    value = shuffle,
//                    onValueChange = { shuffle = it },
//                    label = { Text("shuffle key") },
//                    singleLine = true
//                )
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