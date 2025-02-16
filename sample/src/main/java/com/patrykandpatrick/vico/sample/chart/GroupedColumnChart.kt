/*
 * Copyright 2022 by Patryk Goworowski and Patrick Michalik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.patrykandpatrick.vico.sample.chart

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.shape.roundedCornerShape
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.decoration.ThresholdLine
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.databinding.GroupedColumnChartBinding
import com.patrykandpatrick.vico.sample.extension.fromEntityColors
import com.patrykandpatrick.vico.sample.util.marker

@Composable
internal fun ComposeGroupedColumnChart(
    chartEntryModelProducer: ChartEntryModelProducer,
    modifier: Modifier = Modifier,
) {
    val chartStyle = ChartStyle.fromEntityColors(entityColors = entityColors)
    val decorations = listOf(rememberGroupedColumnChartThresholdLine())
    ProvideChartStyle(chartStyle = chartStyle) {
        val bottomAxis = bottomAxis(valueFormatter = rememberGroupedColumnChartAxisValueFormatter())
        val columnChart = columnChart(
            mergeMode = ColumnChart.MergeMode.Grouped,
            decorations = decorations,
        )
        Chart(
            chart = columnChart,
            chartModelProducer = chartEntryModelProducer,
            modifier = modifier,
            startAxis = startAxis(),
            bottomAxis = bottomAxis,
            marker = marker(),
        )
    }
}

@Composable
internal fun ViewGroupedColumnChart(
    chartEntryModelProducer: ChartEntryModelProducer,
    modifier: Modifier = Modifier,
) {
    val marker = marker()
    val thresholdLine = rememberGroupedColumnChartThresholdLine()
    val axisValueFormatter = rememberGroupedColumnChartAxisValueFormatter()
    AndroidViewBinding(
        factory = GroupedColumnChartBinding::inflate,
        modifier = modifier,
    ) {
        chartView.entryProducer = chartEntryModelProducer
        chartView.marker = marker
        chartView.chart?.setDecorations(decorations = listOf(thresholdLine))
        (chartView.bottomAxis as? HorizontalAxis<AxisPosition.Horizontal.Bottom>)?.valueFormatter = axisValueFormatter
    }
}

@Composable
internal fun rememberGroupedColumnChartThresholdLine(): ThresholdLine {
    val labelComponent = textComponent(
        color = MaterialTheme.colorScheme.surface,
        padding = dimensionsOf(all = THRESHOLD_LINE_PADDING_DP.dp),
        margins = dimensionsOf(all = THRESHOLD_LINE_MARGINS_DP.dp),
        background = ShapeComponent(
            shape = Shapes.roundedCornerShape(all = 4.dp),
            color = THRESHOLD_LINE_COLOR.toInt(),
            strokeWidthDp = 0f,
        ),
    )
    return remember(labelComponent) {
        ThresholdLine(
            thresholdRange = THRESHOLD_RANGE_START..THRESHOLD_RANGE_END,
            labelComponent = labelComponent,
            lineComponent = ShapeComponent(
                color = THRESHOLD_LINE_COLOR
                    .toInt()
                    .copyColor(alpha = THRESHOLD_LINE_ALPHA),
            ),
        )
    }
}

@Composable
internal fun rememberGroupedColumnChartAxisValueFormatter(): AxisValueFormatter<AxisPosition.Horizontal.Bottom> =
    AxisValueFormatter { x, _ -> daysOfWeek[x.toInt() % daysOfWeek.size] }

@Suppress("MagicNumber")
private val entityColors = longArrayOf(0xFF68A7AD, 0xFF99C4C8, 0xFFE5CB9F)
private val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
private const val THRESHOLD_RANGE_START = 7f
private const val THRESHOLD_RANGE_END = 14f
private const val THRESHOLD_LINE_COLOR = 0xFF68A7AD
private const val THRESHOLD_LINE_ALPHA = 0.16f
private const val THRESHOLD_LINE_PADDING_DP = 4f
private const val THRESHOLD_LINE_MARGINS_DP = 4f
