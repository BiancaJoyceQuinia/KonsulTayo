package com.example.healthecare.report


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthecare.global.Global
import com.example.healthecare.ITR.DetailsITR
import com.example.healthecare.R
import com.example.healthecare.adapters.PatAdapter
import com.example.healthecare.models.PatientsModel
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(api = Build.VERSION_CODES.N)
class ViewReport: AppCompatActivity() {

    private lateinit var tvPatients: TextView
    private var countPatients: Int = 0

    private lateinit var dbRef: DatabaseReference

    private lateinit var patRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var startDate: EditText
    private lateinit var endDate: EditText
    private lateinit var btnStartDate: Button
    private lateinit var btnEndDate: Button
    private lateinit var btnSearchDate: Button
    private var list: ArrayList<PatientsModel> = ArrayList<PatientsModel>()

    private lateinit var recordCount: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_report)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Reports of Health Records"

        tvPatients = findViewById(R.id.tvPatients)

        dbRef = FirebaseDatabase.getInstance().reference.child("MainConsultations")

        patRecyclerView = findViewById(R.id.rvPatients)
        patRecyclerView.layoutManager = LinearLayoutManager(this)
        patRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        startDate = findViewById(R.id.startDate)
        endDate = findViewById(R.id.endDate)
        btnStartDate = findViewById(R.id.btnStartDate)
        btnEndDate = findViewById(R.id.btnEndDate)
        btnSearchDate = findViewById(R.id.btnSearchDate)

        recordCount  = findViewById(R.id.recordCount)

        val date = Calendar.getInstance()
        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH)
        val day = date.get(Calendar.DAY_OF_MONTH)


        dbRef.addValueEventListener(object: ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    countPatients = snapshot.childrenCount.toInt()
//                    tvPatients.text = Integer.toString(countPatients) + " Patients"
                    tvPatients.text = "$countPatients Patients"
                }else{
                    tvPatients.text = "O Patients"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        btnStartDate.setOnClickListener {
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->

                var mMonthX = ""
                var mDayX = ""

                if((mMonth + 1) < 10){
                    mMonthX = "0" + (mMonth.plus(1))
                }else{
                    mMonthX = mMonth.plus(1).toString()
                }

                if(mDay < 10){
                    mDayX = "0$mDay"
                }else{
                    mDayX = mDay.toString()
                }

                startDate.setText("$mYear-$mMonthX-$mDayX")

                val input1: String = startDate.text.toString()
                val input2: String = endDate.text.toString()

                btnSearchDate.isEnabled = !(input1.isEmpty() || input2.isEmpty())

            }, year, month, day)
            datePicker.show()
        }

        btnEndDate.setOnClickListener {
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->

                var mMonthX = ""
                var mDayX = ""

                if((mMonth + 1) < 10){
                    mMonthX = "0" + (mMonth.plus(1))
                }else{
                    mMonthX = mMonth.plus(1).toString()
                }

                if(mDay < 10){
                    mDayX = "0$mDay"
                }else{
                    mDayX = mDay.toString()
                }

                endDate.setText("$mYear-$mMonthX-$mDayX")

                val input1: String = startDate.text.toString()
                val input2: String = endDate.text.toString()

                btnSearchDate.isEnabled = !(input1.isEmpty() || input2.isEmpty())
//                if (input1.isEmpty() || input2.isEmpty()) {
//                    btnSearchDate.isEnabled = false
//                } else {
//                    btnSearchDate.isEnabled = true
//                }

            }, year, month, day)
            datePicker.show()
        }

        btnSearchDate.setOnClickListener(View.OnClickListener {
//            getPatientsData()
            openConfirm()
        })
//        getPatientsData()
    }

    private fun openConfirm() {
        val view: View = LayoutInflater.from(this@ViewReport).inflate(R.layout.layout_confirm_update, null)
        val builder = AlertDialog.Builder(this@ViewReport, R.style.AlertDialogTheme).setView(view)
        builder.setView(view)

        (view.findViewById<View>(R.id.textTitle1) as TextView).text =
            resources.getString(R.string.confirm_update)

        val passConfirm = view.findViewById<EditText>(R.id.passConfirm)
        val alertDialog = builder.create()

        val dbRef = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("pass").limitToLast(1)
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (patSnap in snapshot.children) {

                        val userKey = patSnap?.key.toString()

                        Global.userKey = userKey

                        FirebaseDatabase.getInstance().reference.child("Users/$userKey").get().addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                val snapshot = task.result
                                val xpass = snapshot.child("pass").value.toString()

                                view.findViewById<View>(R.id.btnConfirm).setOnClickListener {

                                    val getPass : String = passConfirm.getText().toString()

                                    if (getPass == xpass) {
                                        Toast.makeText(applicationContext, "Correct Password", Toast.LENGTH_SHORT).show()
                                        getPatientsData()
                                        alertDialog.dismiss()

                                    } else {
                                        Toast.makeText(applicationContext, "Incorrect Password", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                view.findViewById<View>(R.id.btnCancel).setOnClickListener {
                                    alertDialog.dismiss()
                                }

                                if (alertDialog.window != null) {
                                    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
                                }
                                alertDialog.show()
                            }
                        }
                    }
                }else{
                    Toast.makeText(applicationContext, "Invalid", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getPatientsData() {

        patRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val input1: String = startDate.text.toString()
        val input2: String = endDate.text.toString()

        var startDate = ""
        var endDate = ""

        if (input1.isEmpty() || input2.isEmpty()) {

            startDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
            endDate = SimpleDateFormat("yyyy-MM-dd").format(Date())

        } else {
            startDate = input1
            endDate = input2
        }

        val dbRef = FirebaseDatabase.getInstance().reference.child("ReportConsultations").orderByChild("vitrDateOfConsult").startAt(startDate).endAt(endDate)

        dbRef.addValueEventListener(object : ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {

                list.clear()

                if(snapshot.exists()){

                    snapshot.childrenCount
                    recordCount.text = snapshot.childrenCount.toString() + " - Record Found"

                    for(patSnap in snapshot.children){
                        val patData = patSnap.getValue(PatientsModel::class.java)
                        list.add(patData!!)
                    }

                    val mAdapter = PatAdapter(list)

                    //recordCount.text = list.count().toString() + " - Record Found"
                    patRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : PatAdapter.onItemClickListener{

                        override fun onItemClick(position: Int) {

                            Global.mainPatientKey = list[position].MainPatientKey.toString()
                            Global.mainConsultationsKey = list[position].MainConsultationsKey.toString()

//                            intent.putExtra("MainConsultationsKey", list[position].MainConsultationsKey)
//                            intent.putExtra("MainPatientKey", list[position].MainPatientKey)

                            val intent = Intent(this@ViewReport, DetailsITR::class.java)
                            startActivity(intent)
                        }
                    })

                    patRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}