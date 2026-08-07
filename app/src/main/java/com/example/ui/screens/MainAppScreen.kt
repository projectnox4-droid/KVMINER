package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.example.ui.components.bounceClick
import kotlinx.coroutines.launch

enum class Screen {
    SPLASH, HOME, SHOP, LUCKY_KV, WITHDRAW, SETTINGS, APP_INFO, RULES, MINER_LIST
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(Screen.SPLASH) }
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler(enabled = currentScreen == Screen.MINER_LIST) {
        currentScreen = Screen.HOME
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.6f),
                drawerContainerColor = MaterialTheme.colorScheme.background,
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
            ) {
                DrawerContent(
                    currentScreen = currentScreen,
                    onScreenSelected = {
                        currentScreen = it
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.blur(if (drawerState.isOpen) 4.dp else 0.dp),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                if (currentScreen != Screen.MINER_LIST && currentScreen != Screen.SPLASH) {
                    AppHeader(
                        onMenuClick = { 
                            if (drawerState.isClosed) {
                                scope.launch { drawerState.open() } 
                            }
                        },
                        onNewsClick = {
                            scope.launch { snackbarHostState.showSnackbar("Fitur News akan hadir segera!") }
                        }
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AnimatedContent(
                    targetState = currentScreen,
                    label = "ScreenTransition",
                    transitionSpec = {
                        if (initialState == Screen.SPLASH && targetState == Screen.HOME) {
                            (fadeIn(animationSpec = tween(500)) + slideInVertically(
                                initialOffsetY = { 200 }, animationSpec = tween(500)
                            )).togetherWith(fadeOut(animationSpec = tween(500)))
                        } else {
                            fadeIn(animationSpec = tween(300)).togetherWith(fadeOut(animationSpec = tween(300)))
                        }
                    }
                ) { screen ->
                    when (screen) {
                        Screen.SPLASH -> SplashScreen(onSplashFinished = { currentScreen = Screen.HOME })
                        Screen.HOME -> HomeScreen(onNavigateToMinerList = { currentScreen = Screen.MINER_LIST })
                        Screen.SHOP -> ShopScreen()
                        Screen.LUCKY_KV -> LuckyKVScreen()
                        Screen.WITHDRAW -> ComingSoonScreen("Withdraw", "Fitur ini akan hadir pada update berikutnya.", onBack = { currentScreen = Screen.HOME })
                        Screen.SETTINGS -> SettingsScreen()
                        Screen.APP_INFO -> AppInfoScreen()
                        Screen.RULES -> RulesScreen()
                        Screen.MINER_LIST -> MinerListScreen(onNavigateBack = { currentScreen = Screen.HOME })
                    }
                }
            }
        }
    }
}

@Composable
fun AppHeader(onMenuClick: () -> Unit, onNewsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Menu Button
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                .bounceClick { onMenuClick() }
                .testTag("burger_menu")
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        
        // Title (approximating gradient with solid for text, or simple styling)
        Text(
            text = "KVMINER",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = (-0.5).sp
        )
        
        // News Button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f))
                .bounceClick { onNewsClick() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "NEWS",
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun DrawerContent(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "KVMINER",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 12.dp)
        )
        
        DrawerItem(
            title = "Home",
            icon = Icons.Default.Home,
            isSelected = currentScreen == Screen.HOME,
            onClick = { onScreenSelected(Screen.HOME) }
        )
        DrawerItem(
            title = "Shop",
            icon = Icons.Default.ShoppingCart,
            isSelected = currentScreen == Screen.SHOP,
            onClick = { onScreenSelected(Screen.SHOP) }
        )
        DrawerItem(
            title = "Lucky KV",
            icon = Icons.Default.Star,
            isSelected = currentScreen == Screen.LUCKY_KV,
            onClick = { onScreenSelected(Screen.LUCKY_KV) }
        )
        DrawerItem(
            title = "Withdraw",
            icon = Icons.Default.AccountBalanceWallet,
            isSelected = currentScreen == Screen.WITHDRAW,
            onClick = { onScreenSelected(Screen.WITHDRAW) }
        )
        DrawerItem(
            title = "Settings",
            icon = Icons.Default.Settings,
            isSelected = currentScreen == Screen.SETTINGS,
            onClick = { onScreenSelected(Screen.SETTINGS) }
        )
        DrawerItem(
            title = "Info Aplikasi",
            icon = Icons.Default.Info,
            isSelected = currentScreen == Screen.APP_INFO,
            onClick = { onScreenSelected(Screen.APP_INFO) }
        )
        DrawerItem(
            title = "Aturan",
            icon = Icons.Default.Description,
            isSelected = currentScreen == Screen.RULES,
            onClick = { onScreenSelected(Screen.RULES) }
        )
    }
}

@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .bounceClick { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
