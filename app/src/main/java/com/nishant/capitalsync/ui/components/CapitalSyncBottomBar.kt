package com.nishant.capitalsync.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nishant.capitalsync.R
import com.nishant.capitalsync.navigation.Dashboard
import com.nishant.capitalsync.navigation.Goals
import com.nishant.capitalsync.navigation.Insights
import com.nishant.capitalsync.navigation.Transactions

@Composable
fun CapitalSyncBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem("Home", R.drawable.home_24px, Dashboard),
        BottomNavItem("History", R.drawable.list_24px, Transactions),
        BottomNavItem("Goals", R.drawable.star_shine_24px, Goals),
        BottomNavItem("Insights", R.drawable.search_insights_24px, Insights)
    )

    // Floating pill-shaped bottom bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .height(68.dp)
                .fillMaxWidth(),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            tonalElevation = 12.dp,
            shadowElevation = 16.dp,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentDestination?.hasRoute(item.route::class) == true

                    // Dynamically animate the weight so the selected item gets more width for its label
                    val weight by animateFloatAsState(
                        targetValue = if (isSelected) 1.5f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessLow),
                        label = "ItemWeight"
                    )

                    CapitalSyncBottomNavItem(
                        item = item,
                        isSelected = isSelected,
                        modifier = Modifier.weight(weight),
                        onClick = {
                            if (!isSelected) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CapitalSyncBottomNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f) else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "BgColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "ContentColor"
    )

    val iconSize by animateDpAsState(
        targetValue = if (isSelected) 26.dp else 24.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "IconSize"
    )

    Box(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .height(48.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = if (isSelected) 12.dp else 4.dp)
        ) {
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.name,
                modifier = Modifier.size(iconSize),
                tint = contentColor
            )

            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandHorizontally(animationSpec = spring(stiffness = Spring.StiffnessLow)),
                exit = fadeOut() + shrinkHorizontally(animationSpec = spring(stiffness = Spring.StiffnessLow))
            ) {
                Text(
                    text = item.name,
                    color = contentColor,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    ),
                    modifier = Modifier.padding(start = 8.dp),
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

data class BottomNavItem(
    val name: String,
    val iconRes: Int,
    val route: Any
)