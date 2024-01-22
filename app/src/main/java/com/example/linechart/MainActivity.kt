package com.example.linechart


import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val colors = listOf(

            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.MAGENTA,
            Color.RED
        )

        fun generateEntries(yValues: List<Int>): List<Entry> {
            val entries = mutableListOf<Entry>()

            for ((index, yValue) in yValues.withIndex()) {
                val xValue = (index + 2).toFloat()
                entries.add(Entry(xValue, yValue.toFloat()))
            }

            return entries
        }

        val lineChart: LineChart = findViewById(R.id.lineChart)

        val yValues = listOf(17,8,7,10,5,2,7,1,10,12,)

        val entries = generateEntries(yValues)




//        val entries = mutableListOf<Entry>()
//        entries.add(Entry(1f, 10f))
//        entries.add(Entry(2f, 20f))
//        entries.add(Entry(3f, 3f))
//        entries.add(Entry(4f, 25f))
//        entries.add(Entry(5f, 18f))



        // Create a dataset and set properties
        val dataSet = LineDataSet(entries, "Data Set")

       // dataSet.color = Color.BLUE
        //dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

//        for ((index, entry) in entries.withIndex()) {
//            entry.icon = null // Clear any existing icon
//            dataSet.setColor(index % colors.size, colors[index % colors.size]) // Set color for the entry
//        }
        dataSet.colors = colors
        //set circle properties
        dataSet.setDrawCircles(true)
        dataSet.setCircleColor(Color.GREEN)
        dataSet.circleRadius=7f
        dataSet.setDrawCircleHole(true)
        dataSet.circleHoleColor=Color.RED
        dataSet.circleHoleRadius=3f
        //set text properties
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize=15f

        //set line width
        dataSet.lineWidth=5f

//        for ((index, entry) in entries.withIndex()) {
//            entry.icon = null // Clear any existing icon
//            dataSet.setColor(colors[index % colors.size]) // Set color for the entry
//        }

        // Create a LineData object and add the dataset to it
        val lineData = LineData(dataSet)

        // Set data to the chart
        lineChart.data = lineData

        // Customize chart appearance and behavior as needed
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.setNoDataText("No data found")
        lineChart.setNoDataTextColor(Color.GREEN)

        // Enable drawing the grid background
        lineChart.setDrawGridBackground(true)
        //lineChart.setGridBackgroundColor(Color.GREEN)




        // drawing borders
        lineChart.setDrawBorders(true)
        lineChart.setBorderColor(Color.GREEN)
        lineChart.setBorderWidth(3f)

        // Set chart description
        val description = Description()
        description.text = "My Line Chart"
        description.textSize= 20f
        description.textColor=Color.BLUE

        lineChart.description = description



        //legend
        val legend: Legend = lineChart.legend
        legend.form = Legend.LegendForm.LINE // Set legend forms to lines
        legend.textColor = Color.RED
        legend.textSize=15f
        legend.form=Legend.LegendForm.CIRCLE
        legend.formSize=15f

        val markerView = CustomMarkerView(this, R.layout.custom_marker_view)
        lineChart.marker = markerView


        //axis formatter
        val xAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        val leftYAxis = lineChart.axisLeft
        leftYAxis.setDrawGridLines(false)
        val rightYAxis = lineChart.axisRight
        rightYAxis.setDrawGridLines(false)

        xAxis.labelCount = 0
        leftYAxis.labelCount=0
        rightYAxis.labelCount=0

        xAxis.valueFormatter = XAxisFormatter()
        leftYAxis.valueFormatter = YAxisFormatter()
        rightYAxis.valueFormatter = YAxisFormatter()


        // Animate the chart
        lineChart.animateX(1000, Easing.EaseInOutQuart)
        lineChart.animateY(1000, Easing.EaseInOutQuart)

        // Animate the chart gradually

      //  animateChart(lineChart, entries)







        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                e?.let {
                    val description = e.y as? String
                    description?.let {


                            (lineChart.marker as CustomMarkerView).showMarkerView(true)

                        }
                    }
                (lineChart.marker as CustomMarkerView).showMarkerView(false)
            }



//            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                // Handle the click event here
//                e?.let {
//                    val description = e.data as? String
//                    description?.let {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Clicked on point: $description",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//

            override fun onNothingSelected() {
                (lineChart.marker as CustomMarkerView).showMarkerView(false)
                lineChart.highlightValues(null)
            }
        })


        // Invalidate the chart to refresh
        lineChart.invalidate()

    }
//    fun animateChart(lineChart: LineChart, entries: List<Entry>) {
//        val animator = ValueAnimator.ofFloat(0f, 1f)
//        animator.duration = 5000 // Animation duration in milliseconds
//        animator.addUpdateListener { animation ->
//            val progress = animation.animatedValue as Float
//            // Update the Y values of entries gradually
//            for ((index, entry) in entries.withIndex()) {
//                entry.y = progress * index * 5 // Adjust the multiplier as needed
//            }
//            // Notify the chart that the data has changed
//            lineChart.data.notifyDataChanged()
//            lineChart.notifyDataSetChanged()
//            // Move the chart to the last entry
//            lineChart.moveViewToX(entries.size.toFloat())
//        }
//        animator.start()
//    }



    // You can define your custom axis formatters if needed

    private class XAxisFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {

            return "Day $value"
        }
    }

    private class YAxisFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {

            return " $value"
        }
    }


    private class CustomMarkerView(context: android.content.Context, layoutResource: Int) :
        com.github.mikephil.charting.components.MarkerView(context, layoutResource) {

        private val tvContent: TextView = findViewById(R.id.tvContent)

        override fun refreshContent(e: Entry?, highlight: Highlight?) {
            e?.let {
                tvContent.text = "Value: ${e.y}"
            }
            super.refreshContent(e, highlight)
        }
        private var showMarker = false
        fun showMarkerView(show: Boolean) {
            showMarker = show
            visibility = if (show) View.VISIBLE else View.GONE
        }

//        override fun getOffset(): MPPointF {
//            return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
//        }
    }

}