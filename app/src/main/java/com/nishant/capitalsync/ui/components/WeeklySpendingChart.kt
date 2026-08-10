package com.nishant.capitalsync.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nishant.capitalsync.R
import com.nishant.capitalsync.ui.viewmodel.WeeklySpendingDataPoint
import com.nishant.capitalsync.utils.CategoryUtils
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklySpendingChart(
    spendingData: List<WeeklySpendingDataPoint>,
    weekRange: String,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    isNextEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val totalLineColor = MaterialTheme.colorScheme.primary
    val animationProgress = remember { Animatable(0f) }

    // State to track which categories are visible
    val hiddenCategories = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(spendingData) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Week Navigation Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPreviousWeek) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_left_24px),
                        contentDescription = "Previous Week",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Weekly Spending",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = weekRange,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onNextWeek, enabled = isNextEnabled) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_right_24px),
                        contentDescription = "Next Week",
                        tint = if (isNextEnabled) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (spendingData.all { it.totalAmount == 0.0 }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No spending recorded for this week",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray.copy(alpha = 0.5f)
                    )
                }
            } else {
                val maxAmount = remember(spendingData) {
                    val maxVal = spendingData.maxOfOrNull { it.totalAmount } ?: 0.0
                    val magnitude = max(10.0, maxVal)
                    (ceil(magnitude / 10.0) * 10.0).coerceAtLeast(10.0)
                }

                val categoryInfo = remember(spendingData) {
                    spendingData.flatMap { it.categorySpent }
                        .associateBy({ it.categoryName }, { it.iconResId })
                }
                val sortedCategoryNames = remember(categoryInfo) {
                    val names = categoryInfo.keys.toMutableList()
                    names.remove("Other")
                    names.sort()
                    if (categoryInfo.containsKey("Other")) {
                        names.add("Other")
                    }
                    names
                }

                val categoryColors = remember(sortedCategoryNames) {
                    sortedCategoryNames.associateWith { name ->
                        CategoryUtils.getCategoryColor(name)
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                    // Y-Axis Labels
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(35.dp)
                            .padding(bottom = 20.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        val gridCount = 4
                        val step = maxAmount / gridCount
                        for (i in gridCount downTo 0) {
                            Text(
                                text = String.format(Locale.US, "%.0f", i * step),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // The Graph Area
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 40.dp, bottom = 20.dp, top = 5.dp, end = 10.dp)
                    ) {
                        val width = size.width
                        val height = size.height
                        val spacingX = width / (spendingData.size - 1)
                        val scaleY = height / maxAmount.toFloat()

                        // Grid
                        val gridCount = 4
                        for (i in 0..gridCount) {
                            val y = height - (i * (height / gridCount))
                            drawLine(
                                color = Color.LightGray.copy(alpha = 0.2f),
                                start = Offset(0f, y),
                                end = Offset(width, y),
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        fun getPoints(dataSelector: (WeeklySpendingDataPoint) -> Double): List<Offset> {
                            return spendingData.mapIndexed { index, data ->
                                Offset(
                                    x = index * spacingX,
                                    y = height - (dataSelector(data).toFloat() * scaleY * animationProgress.value)
                                )
                            }
                        }

                        // Draw Category lines
                        sortedCategoryNames.forEach { catName ->
                            if (hiddenCategories[catName] != true) {
                                val color = categoryColors[catName] ?: Color.Gray
                                val points = getPoints { it.categorySpent.find { cp -> cp.categoryName == catName }?.amount ?: 0.0 }
                                drawSmoothLine(points, color.copy(alpha = 0.8f), strokeWidth = 1.dp.toPx())
                            }
                        }

                        // Draw Total line
                        if (hiddenCategories["All"] != true) {
                            val totalPoints = getPoints { it.totalAmount }
                            drawSmoothLine(
                                points = totalPoints,
                                color = totalLineColor.copy(alpha = 0.5f),
                                strokeWidth = 1.5.dp.toPx(),
                                isDotted = true
                            )
                            totalPoints.forEach { point ->
                                drawCircle(color = Color.White.copy(alpha = 0.5f), radius = 3.dp.toPx(), center = point)
                                drawCircle(color = totalLineColor.copy(alpha = 0.5f), radius = 3.dp.toPx(), center = point, style = Stroke(width = 1.5.dp.toPx()))
                            }
                        }
                    }

                    // X-Axis Labels (Days)
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        spendingData.forEach { data ->
                            Text(
                                text = data.dayLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                modifier = Modifier.width(36.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Static Wrapping Legend
                SimpleFlowRow(horizontalGap = 8.dp, verticalGap = (-5).dp) {
                    CategoryChip(
                        label = "All",
                        color = totalLineColor,
                        selected = hiddenCategories["All"] != true,
                        iconResId = null,
                        onToggle = { hiddenCategories["All"] = (hiddenCategories["All"] == true).let { !it } }
                    )

                    sortedCategoryNames.forEach { name ->
                        CategoryChip(
                            label = name,
                            color = categoryColors[name] ?: Color.Gray,
                            selected = hiddenCategories[name] != true,
                            iconResId = categoryInfo[name],
                            onToggle = { hiddenCategories[name] = (hiddenCategories[name] == true).let { !it } }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleFlowRow(
    horizontalGap: Dp = 10.dp,
    verticalGap: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Layout(content = content) { measurables, constraints ->
        val xGap = horizontalGap.roundToPx()
        val yGap = verticalGap.roundToPx()
        val rows = mutableListOf<List<Placeable>>()
        val rowHeights = mutableListOf<Int>()
        var currentRow = mutableListOf<Placeable>()
        var currentRowWidth = 0
        var currentRowHeight = 0

        measurables.forEach { measurable ->
            val placeable = measurable.measure(constraints.copy(minWidth = 0))
            if (currentRowWidth + placeable.width > constraints.maxWidth && currentRow.isNotEmpty()) {
                rows.add(currentRow)
                rowHeights.add(currentRowHeight)
                currentRow = mutableListOf()
                currentRowWidth = 0
                currentRowHeight = 0
            }
            currentRow.add(placeable)
            currentRowWidth += placeable.width + xGap
            currentRowHeight = max(currentRowHeight, placeable.height)
        }
        rows.add(currentRow)
        rowHeights.add(currentRowHeight)

        val height = (rowHeights.sum() + (rows.size - 1) * yGap).coerceAtLeast(0)
        layout(constraints.maxWidth, height) {
            var y = 0
            rows.forEachIndexed { i, row ->
                var x = 0
                row.forEach { placeable ->
                    placeable.placeRelative(x, y)
                    x += placeable.width + xGap
                }
                y += rowHeights[i] + yGap
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryChip(
    label: String,
    color: Color,
    selected: Boolean,
    iconResId: Int?,
    onToggle: () -> Unit
) {
    Surface(
        onClick = onToggle,
        selected = selected,
        shape = RoundedCornerShape(8.dp),
        color = if (selected) color.copy(alpha = 0.1f) else Color.Transparent,
        border = BorderStroke(
            1.dp,
            if (selected) color.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.3f)
        ),
        modifier = Modifier.wrapContentSize()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconResId != null) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (selected) color else Color.Gray.copy(alpha = 0.5f)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(if (selected) color.copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) MaterialTheme.colorScheme.onSurface else Color.Gray,
                maxLines = 1
            )
        }
    }
}

private fun DrawScope.drawSmoothLine(
    points: List<Offset>,
    color: Color,
    strokeWidth: Float,
    isDotted: Boolean = false
) {
    if (points.size < 2) return
    val path = Path()
    path.moveTo(points[0].x, points[0].y)

    for (i in 0 until points.size - 1) {
        val p1 = points[i]
        val p2 = points[i + 1]
        val controlPoint1 = Offset(p1.x + (p2.x - p1.x) / 2f, p1.y)
        val controlPoint2 = Offset(p1.x + (p2.x - p1.x) / 2f, p2.y)
        path.cubicTo(
            controlPoint1.x, controlPoint1.y,
            controlPoint2.x, controlPoint2.y,
            p2.x, p2.y
        )
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = if (isDotted) PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f) else null
        )
    )
}