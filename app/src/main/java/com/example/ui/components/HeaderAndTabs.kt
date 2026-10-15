package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.MainTab
import com.example.ui.theme.*
import com.example.ui.util.IntentUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdealTopBar(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(IdealNavyDark),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ideal_icon),
                            contentDescription = "Ideal Technologies Logo",
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "IDEAL",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "TECH",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = IdealOrange
                            )
                        }
                        Text(
                            text = "idealtechnologies.co.zw",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Dark Mode toggle
                    IconButton(
                        onClick = onToggleDarkMode,
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("dark_mode_toggle")
                    ) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Dark Mode",
                            tint = if (isDarkMode) IdealOrange else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Direct WhatsApp Connect button
                    FilledTonalButton(
                        onClick = { IntentUtils.openWhatsApp(context) },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color(0xFF25D366).copy(alpha = 0.15f),
                            contentColor = Color(0xFF1E7E34)
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("top_whatsapp_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "WhatsApp",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF25D366)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Chat",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isDarkMode) Color(0xFF25D366) else Color(0xFF0F5132)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IdealBottomNavigation(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("bottom_nav_bar")
    ) {
        NavigationBarItem(
            selected = currentTab == MainTab.HOME,
            onClick = { onTabSelected(MainTab.HOME) },
            icon = {
                Icon(
                    imageVector = if (currentTab == MainTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home Tab"
                )
            },
            label = { Text("Home", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = IdealOrange,
                indicatorColor = IdealOrange
            ),
            modifier = Modifier.testTag("nav_home")
        )

        NavigationBarItem(
            selected = currentTab == MainTab.SERVICES,
            onClick = { onTabSelected(MainTab.SERVICES) },
            icon = {
                Icon(
                    imageVector = if (currentTab == MainTab.SERVICES) Icons.Filled.GridView else Icons.Outlined.GridView,
                    contentDescription = "Services Tab"
                )
            },
            label = { Text("Services", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = IdealOrange,
                indicatorColor = IdealOrange
            ),
            modifier = Modifier.testTag("nav_services")
        )

        NavigationBarItem(
            selected = currentTab == MainTab.AI_ASSISTANT,
            onClick = { onTabSelected(MainTab.AI_ASSISTANT) },
            icon = {
                BadgedBox(
                    badge = {
                        Badge(containerColor = IdealCyan) {
                            Text("AI", color = IdealNavyDark, fontSize = 9.sp, fontWeight = FontWeight.Black)
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentTab == MainTab.AI_ASSISTANT) Icons.Filled.SmartToy else Icons.Outlined.SmartToy,
                        contentDescription = "Ideal Inside AI Tab"
                    )
                }
            },
            label = { Text("Ideal AI", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = IdealOrange,
                indicatorColor = IdealOrange
            ),
            modifier = Modifier.testTag("nav_ai")
        )

        NavigationBarItem(
            selected = currentTab == MainTab.CLIENT_PORTAL,
            onClick = { onTabSelected(MainTab.CLIENT_PORTAL) },
            icon = {
                Icon(
                    imageVector = if (currentTab == MainTab.CLIENT_PORTAL) Icons.Filled.TrackChanges else Icons.Outlined.TrackChanges,
                    contentDescription = "Portal & Trackers Tab"
                )
            },
            label = { Text("Portal", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = IdealOrange,
                indicatorColor = IdealOrange
            ),
            modifier = Modifier.testTag("nav_portal")
        )

        NavigationBarItem(
            selected = currentTab == MainTab.CONTACT,
            onClick = { onTabSelected(MainTab.CONTACT) },
            icon = {
                Icon(
                    imageVector = if (currentTab == MainTab.CONTACT) Icons.Filled.AccountBalanceWallet else Icons.Outlined.AccountBalanceWallet,
                    contentDescription = "Contact & Pay Tab"
                )
            },
            label = { Text("Contact", style = MaterialTheme.typography.labelSmall) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = IdealOrange,
                indicatorColor = IdealOrange
            ),
            modifier = Modifier.testTag("nav_contact")
        )
    }
}
