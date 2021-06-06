package pl.patrykgoworowski.liftchart_common.axis.formatter

import pl.patrykgoworowski.liftchart_common.data_set.entry.collection.EntriesModel

object DefaultAxisFormatter : AxisValueFormatter {

    override fun formatValue(value: Float, index: Int, model: EntriesModel): String = value.toString()

}