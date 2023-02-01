package com.example.healthecare.activities

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.healthecare.R
//import com.github.mikephil.charting.charts.BarChart
//import com.github.mikephil.charting.data.BarData
//import com.github.mikephil.charting.data.BarDataSet
//import com.github.mikephil.charting.data.BarEntryZ

class Analytics : AppCompatActivity() {

//    // on below line we are creating
//    // variables for our bar chart
//    lateinit var barChart: BarChart
//
//    // on below line we are creating
//    // a variable for bar data
//    lateinit var barData: BarData
//
//    // on below line we are creating a
//    // variable for bar data set
//    lateinit var barDataSet: BarDataSet
//
//    // on below line we are creating array list for bar data
//    lateinit var barEntriesList: ArrayList<BarEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)


//        // on below line we are initializing
//        // our variable with their ids.
//        barChart = findViewById(R.id.idBarChart)
//
//        // on below line we are calling get bar
//        // chart data to add data to our array list
//
//        barEntriesList.add(BarEntry(0f, 10f, "AA"))
//        barEntriesList.add(BarEntry(1f, 20f, "BB"))
//        barEntriesList.add(BarEntry(2f, 30f, "CC"))
//        barEntriesList.add(BarEntry(3f, 40f, "DD"))
//        barEntriesList.add(BarEntry(4f, 50f, "EE"))
//
//
//        // on below line we are initializing our bar data set
//        barDataSet = BarDataSet(barEntriesList, "Bar Chart Data")
//
//        // on below line we are initializing our bar data
//
////        val labels = ArrayList<String>()
////        labels.add("Total")
////        labels.add("Obtained")
////        labels.add("Highest")
////        labels.add("Average")
////        labels.add("Average")
//
//
//        val labels = arrayListOf(
//            "Ene", "Feb", "Mar",
//            "Abr", "May"
//        )
////        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
////        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//
//
//        barChart.setDrawGridBackground(false)
//        barChart.axisLeft.isEnabled = false
//        barChart.axisRight.isEnabled = false
//        barChart.description.isEnabled = false
//
//        barData = BarData(barDataSet)
//
//
//
//        // on below line we are setting data to our bar chart
//        barChart.data = barData
//
//        // on below line we are setting colors for our bar chart text
//        barDataSet.valueTextColor = Color.BLACK
//
//        // on below line we are setting color for our bar data set
//        barDataSet.setColor(resources.getColor(R.color.purple_200))
//
//        // on below line we are setting text size
//        barDataSet.valueTextSize = 12f
//
//        // on below line we are enabling description as false
//        barChart.description.isEnabled = true
//
//
//
//        barChart.invalidate()

    }


    private fun getBarChartData() {
        //barEntriesList = ArrayList()

//        val dbRef = FirebaseDatabase.getInstance().reference.child("AnalyticConsultations")
//
//        dbRef.addValueEventListener(object : ValueEventListener {
//            @SuppressLint("SetTextI18n")
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                if(snapshot.exists()){
//
//                    //snapshot.childrenCount
//                    //recordCount.text = snapshot.childrenCount.toString() + " - Record Found"
//
////                    val fields = HashMap<String, Any>()
////                    fields.clear()
//
//                    val fieldsP = HashMap<Any, HashMap<String, Any>>()
//                    fieldsP.clear()
//
//                    val fieldsC = HashMap<String, Any>()
//
//                    for(patSnap in snapshot.children){
//
//                        fieldsC.clear()
//                        //println(patSnap.key.toString())
//
//                        if(patSnap.key.toString() == "vitrDiagnosis"){
//
//                            for(patSnap2 in patSnap.children){
//                                println(patSnap2.key + " - " + patSnap2.value)
//                                fieldsC[patSnap2.key.toString()] = patSnap2.value.toString()
//
//                                fieldsP["vitrDiagnosis"] = fieldsC
//                            }
//
//
//                        }
//
//                        //println(fieldsC)
//                    }
//
//
//                    println(fieldsP)
//
//
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })

        // on below line we are adding data
        // to our bar entries list
//        barEntriesList.add(BarEntry(0f, 10f, "AA"))
//        barEntriesList.add(BarEntry(1f, 20f, "BB"))
//        barEntriesList.add(BarEntry(2f, 30f, "CC"))
//        barEntriesList.add(BarEntry(3f, 40f, "DD"))
//        barEntriesList.add(BarEntry(4f, 50f, "EE"))





//        val data = BarData(labels, barDataSet)
//        barChart!!.data = data
//        barChart!!.setDescription("")
//        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)

    }

}