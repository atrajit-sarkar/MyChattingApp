package com.example.mychattingapp.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mychattingapp.LocaldbLogics.ViewModel.ChatAppViewModel
import com.example.mychattingapp.NavHost.navigateIfNotFast
import com.example.mychattingapp.ui.theme.MyChattingAppTheme
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SwipeableScreensWithTabs(
    viewModel: ChatAppViewModel,
    navController: NavController
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val coroutineScope = rememberCoroutineScope()
    val topScreenBar = remember {
        mutableStateOf<@Composable () -> Unit>({})
    }
    val bigActionIcon = remember {
        mutableStateOf<@Composable () -> Unit>({})
    }
    val smallActionIcon = remember {
        mutableStateOf<@Composable () -> Unit>({})
    }
    val bigAction = remember {
        mutableStateOf({})
    }
    val smallAction = remember {
        mutableStateOf({})
    }
    val showDropDown = remember { mutableStateOf(false) }
    val tabColor = remember {
        mutableStateOf(Color(0xFF083259))
    }
    val smallActionIconVisible = remember {
        mutableStateOf(true)
    }


    // Ensure theme is applied consistently
    Scaffold(
        topBar = {
            topScreenBar.value()
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AnimatedVisibility(
                    visible = smallActionIconVisible.value,
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> 2 * fullHeight } // Starts from below the screen
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { fullHeight -> 2 * fullHeight } // Exits to below the screen
                    )
                ) {


                    FloatingActionButton(
                        onClick = {
                            navigateIfNotFast {
                                smallAction.value()
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.background,
                        modifier = Modifier.size(40.dp)
                    ) {

                        smallActionIcon.value()

                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                FloatingActionButton(
                    onClick = {
                        navigateIfNotFast {
                            bigAction.value()
                        }
                    },
                    containerColor = Color.Green
                ) {
                    bigActionIcon.value()
                }
            }
        },
        bottomBar = {
            androidx.compose.material3.BottomAppBar(
                containerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.height(103.dp)
            ) {
                TabRowFunction(
                    pagerState,
                    tabColor,
                    coroutineScope,
                    topScreenBar,
                    navController,
                    showDropDown,
                    viewModel,
                    bigActionIcon, smallActionIcon,
                    smallAction, bigAction,
                    smallActionIconVisible
                )
            }
        }
    ) { innerPadding ->

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> HomeScreen(navController = navController, viewModel = viewModel)
                1 -> UpdateScreen(viewModel, navController)
                2 -> CommunityScreen()
                3 -> CallScreen()
            }
        }
    }

}

@Composable
fun CallScreen() {
    MyChattingAppTheme {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Hello Calls")
        }
    }

}

@Composable
private fun TabRowFunction(
    pagerState: PagerState,
    tabColor: MutableState<Color>,
    coroutineScope: CoroutineScope,
    topScreenBar: MutableState<@Composable () -> Unit>,
    navController: NavController,
    showDropDown: MutableState<Boolean>,
    viewModel: ChatAppViewModel,
    bigActionIcon: MutableState<@Composable () -> Unit>,
    smallActionIcon: MutableState<@Composable () -> Unit>,
    smallAction: MutableState<() -> Unit>,
    bigAction: MutableState<() -> Unit>,
    smallActionIconVisible: MutableState<Boolean>
) {
    val homeScreenSearchBarActiveState by viewModel.homeScreenSearchBarActiveState.collectAsState()


    TabRow(
        divider = {},
        containerColor = Color.Transparent,
        selectedTabIndex = pagerState.currentPage,
        indicator = { tabPositions ->
            val currentTabPosition = tabPositions[pagerState.currentPage]
//            val indicatorOffset by animateDpAsState(currentTabPosition.left, label = "")
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(top = 3.dp)
            ) {

                Card(
                    modifier = Modifier
                        .tabIndicatorOffset(currentTabPosition)
                        .width(currentTabPosition.width)
                        .size(40.dp)
                        .padding(top = 5.dp)
                        .padding(horizontal = 10.dp),
                    colors = CardDefaults.cardColors(Color(0xFF8af488).copy(alpha = 0.2f)),
                    shape = CircleShape
                ) {}
            }
        }
    ) {
        LoadTabsFunction(pagerState, tabColor, coroutineScope)

        when (pagerState.currentPage) {
            0 -> {
                smallActionIconVisible.value = true
                topScreenBar.value = {
                    if (!homeScreenSearchBarActiveState) {

                        HomeTopAppBar(navController, showDropDown, viewModel)
                    }
                }
                bigActionIcon.value = {
                    FaIcon(faIcon = FaIcons.SnapchatGhost)

                }
                smallActionIcon.value = {
                    FaIcon(
                        faIcon = FaIcons.CircleNotch,
                        tint = Color(0xFF47d2ea)
                    )
                }
                bigAction.value = {
                    navController.navigate("allcontact_screen")
                }
                smallAction.value = {

                }
            }

            1 -> {
                smallActionIconVisible.value = true
                topScreenBar.value = { UpdateScreenTopAppBar(viewModel) }
                bigActionIcon.value = {
                    FaIcon(
                        faIcon = FaIcons.Camera
                    )
                }
                smallActionIcon.value = {
                    FaIcon(
                        faIcon = FaIcons.PencilAlt,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                bigAction.value = {

                }
                smallAction.value = {

                }
            }

            else -> {
                smallActionIconVisible.value = false
                topScreenBar.value = {}
                bigActionIcon.value = {
                    FaIcon(
                        faIcon = FaIcons.PeopleArrows
                    )
                }
                bigAction.value = {

                }
            }
        }
    }
}


@Composable
private fun LoadTabsFunction(
    pagerState: PagerState = rememberPagerState(pageCount = { 3 }),
    tabColor: MutableState<Color> = remember { mutableStateOf(Color.LightGray) },
    coroutineScope: CoroutineScope
) {


    loadTabData().forEachIndexed { index, tabdata ->
        Tab(
            text = {
                Text(
                    modifier = Modifier.wrapContentWidth(), // Wraps to the content width
                    text = tabdata.title,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1, // Ensure single line
                    overflow = TextOverflow.Ellipsis, // Use ellipsis if needed
                    textAlign = TextAlign.Center // Center-aligns within the tab
                )
            },
            icon = {

                tabColor.value = MaterialTheme.colorScheme.onBackground


                tabdata.Icon(tabColor.value)

            },
            selected = pagerState.currentPage == index,
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )
    }

}

@Composable
fun CommunityScreen() {
    MyChattingAppTheme {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Hello Community")
        }
    }

}

data class TabData(
    var title: String,
    var Icon: @Composable (Color) -> Unit
)

fun loadTabData(): List<TabData> {
    return listOf(
        TabData(
            title = "Home",
            Icon = { color ->
                FaIcon(
                    faIcon = FaIcons.Rocketchat,
                    tint = color
                )
            }
        ),
        TabData(
            title = "Updates",
            Icon = { color ->
                FaIcon(
                    faIcon = FaIcons.GgCircle,
                    tint = color
                )
            }
        ),
        TabData(
            title = "Communities",
            Icon = { color ->
                FaIcon(
                    faIcon = FaIcons.PeopleArrows,
                    tint = color,
                    size = 20.dp
                )
            }
        ),
        TabData(
            title = "Calls",
            Icon = { color ->
                FaIcon(
                    faIcon = FaIcons.Phone,
                    tint = color
                )
            }
        )

    )
}

