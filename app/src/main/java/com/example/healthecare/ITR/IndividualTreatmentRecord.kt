package com.example.healthecare.ITR

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.healthecare.R
import com.example.healthecare.activities.MainActivity
import com.example.healthecare.global.Global
import com.example.healthecare.models.SpecificDiagnosis
import com.example.healthecare.models.UsersModel
//import com.example.healthecare.databinding.ActivityInsertionBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class IndividualTreatmentRecord : AppCompatActivity() {

    private lateinit var itrId: TextView
    private lateinit var perId: TextView
    private lateinit var itrLName: TextView
    private lateinit var itrFName: TextView
    private lateinit var itrMName: TextView
    private lateinit var itrSuffix: TextView
    private lateinit var itrAge: TextView
    private lateinit var itrSex: TextView
    private lateinit var itrAddress: TextView
    private lateinit var itrModeOfTransac: Spinner
    private lateinit var itrDateOfConsult: TextView
    private lateinit var itrDateOfReturn: TextView

    private lateinit var itrConsultTime: TextView
    private lateinit var itrBP: EditText
    private lateinit var itrTemp: EditText
    private lateinit var itrHeight: EditText
    private lateinit var itrWeight: EditText
    private lateinit var itrTypeOfConsult: Spinner
    private lateinit var itrComplaint: EditText
    private lateinit var itrDiagnosis: EditText
    private lateinit var itrTreatment: EditText

    private lateinit var getMode : String
    private lateinit var getType : String

    private lateinit var btnSave: Button
    private lateinit var btnDate: Button
    private lateinit var btnReturnDate: Button
    private lateinit var btnTime: Button

    private lateinit var dbRef: DatabaseReference

    private var isListItemClicked = true

    var tvSpecificDiagnosis: TextView? = null
    var arrayDiagnosis: ArrayList<String>? = null
    var dialog: Dialog? = null

    private lateinit var temptvSpecificDiagnosis: EditText

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itr_form)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Individual Treatment Record Form"

        val date = Calendar.getInstance()
        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH)
        val day = date.get(Calendar.DAY_OF_MONTH)

        initView()
        diagnosisList()
        passData()
        modeOfTransac()
        typeOfConsult()

        tvSpecificDiagnosis?.setOnClickListener(View.OnClickListener {

//            var dbref = FirebaseDatabase.getInstance().getReference("SpecificDiagnosis")

            dialog = Dialog(this@IndividualTreatmentRecord)
            dialog!!.setContentView(R.layout.itr_dialog_diagnosis)
            dialog!!.window!!.setLayout(750, 1000)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.show()

            val etSearch = dialog!!.findViewById<EditText>(R.id.etSearch)
            val listView = dialog!!.findViewById<ListView>(R.id.listView)

            val adapter = ArrayAdapter(this@IndividualTreatmentRecord, android.R.layout.simple_list_item_1,
                arrayDiagnosis!!)

            listView.adapter = adapter
            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapter.filter.filter(s)
                }
                override fun afterTextChanged(s: Editable) {}
            })

            listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->

                    if(adapter.getItem(position).toString() == "Others"){

                        temptvSpecificDiagnosis.visibility = View.VISIBLE

                    }else{

                        temptvSpecificDiagnosis.visibility = View.GONE
                    }

                    tvSpecificDiagnosis?.setText(adapter.getItem(position))
                    dialog!!.dismiss()
                }
        })

        btnSave.setOnClickListener{
            if(isListItemClicked) {
                savePatientsData()

            }else {
                clearInput()
            }
        }

        btnDate.setOnClickListener {
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->

                var mMonthX = ""
                var mDayX = ""

                if(mMonth + 1 < 10){
                    mMonthX = "0" + (mMonth.plus(1))
                }else{
                    mMonthX = (mMonth.plus(1)).toString()
                }

                if(mDay < 10){
                    mDayX = "0$mDay"
                }else{
                    mDayX = "$mDay"
                }

                itrDateOfConsult.setText("$mYear-$mMonthX-$mDayX")
            }, year, month, day)
            datePicker.show()
        }

        btnReturnDate.setOnClickListener {
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->

                var mMonthX = ""
                var mDayX = ""

                if(mMonth + 1 < 10){
                    mMonthX = "0" + (mMonth.plus(1))
                }else{
                    mMonthX = (mMonth.plus(1)).toString()
                }

                if(mDay < 10){
                    mDayX = "0$mDay"
                }else{
                    mDayX = "$mDay"
                }

                itrDateOfReturn.setText("$mYear-$mMonthX-$mDayX")
            }, year, month, day)
            datePicker.show()
        }

        btnTime.setOnClickListener {
            val cal = Calendar.getInstance()
            val timePicker = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                itrConsultTime.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(this, timePicker, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
    }

    private fun diagnosisList() {

        arrayDiagnosis!!.clear()

        val dbRef = FirebaseDatabase.getInstance().reference.child("SpecificDiagnosis").orderByChild("vitrSpecificDiagnosis")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (patSnap in snapshot.children) {
                        val usersData = patSnap.getValue(SpecificDiagnosis::class.java)
                        val vitrSpecificDiagnosis = usersData?.vitrSpecificDiagnosis
                        arrayDiagnosis!!.add(vitrSpecificDiagnosis.toString())
                    }

                    arrayDiagnosis!!.add("OTHERS")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun initView(){
        itrId = findViewById(R.id.itrId)
        perId = findViewById(R.id.perId)
        itrLName = findViewById(R.id.itrLastName)
        itrFName = findViewById(R.id.itrFirstName)
        itrMName = findViewById(R.id.itrMiddleName)
        itrSuffix = findViewById(R.id.itrSuffix)
        itrAge = findViewById(R.id.itrAge)
        itrSex = findViewById(R.id.itrSex)
        itrAddress = findViewById(R.id.itrAddress)
        itrModeOfTransac = findViewById(R.id.itrMode)
        itrDateOfConsult = findViewById(R.id.itrDate)
        itrDateOfReturn = findViewById(R.id.itrReturnDate)

        itrConsultTime = findViewById(R.id.itrTime)
        itrBP = findViewById(R.id.itrBP)
        itrTemp = findViewById(R.id.itrTemp)
        itrHeight = findViewById(R.id.itrHeight)
        itrWeight = findViewById(R.id.itrWeight)
        itrTypeOfConsult = findViewById(R.id.itrTypeOfConsult)
        itrComplaint = findViewById(R.id.itrComplaint)
        itrDiagnosis = findViewById(R.id.itrDiagnosis)
        itrTreatment = findViewById(R.id.itrTreatment)

        tvSpecificDiagnosis = findViewById(R.id.tvSpecificDiagnosis)
        arrayDiagnosis = ArrayList()

        temptvSpecificDiagnosis = findViewById(R.id.temptvSpecificDiagnosis)

        btnSave = findViewById(R.id.btnSave)
        btnDate = findViewById(R.id.btnDate)
        btnReturnDate = findViewById(R.id.btnReturnDate)
        btnTime = findViewById(R.id.btnTime)
        dbRef = FirebaseDatabase.getInstance().getReference("MainConsultations")
    }

    private fun concat(lastName: String, space: String, firstName: String): String{
        return lastName + space + firstName
    }

    private fun passData(){
        val bundle = intent.extras
        if(bundle != null){
            perId.text = "${bundle.getString("key")}"
            itrLName.text = "${bundle.getString("vperLName")}"
            itrFName.text = "${bundle.getString("vperFName")}"
            itrMName.text = "${bundle.getString("vperMName")}"
            itrSuffix.text = "${bundle.getString("vperSuffix")}"
            itrAddress.text = "${bundle.getString("vperResAdd")}"
            itrAge.text = "${bundle.getString("vperAge")}"
            itrSex.text = "${bundle.getString("vperSex")}"
        }
    }

    private fun modeOfTransac() {
        itrModeOfTransac.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                itrModeOfTransac.onItemSelectedListener = this
                getMode = adapterView?.getItemAtPosition(position).toString()
            }
        }
    }

    private fun typeOfConsult() {
        itrTypeOfConsult.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                itrTypeOfConsult.onItemSelectedListener = this
                getType = adapterView?.getItemAtPosition(position).toString()
            }
        }
    }

    private fun clearInput(){
        itrId.setText("")
        perId.setText("")
        itrLName.setText("")
        itrFName.setText("")
        itrMName.setText("")
        itrSuffix.setText("")
        itrAddress.setText("")
        itrDateOfConsult.setText("")
        itrConsultTime.setText("")
        itrBP.setText("")
        itrTemp.setText("")
        itrHeight.setText("")
        itrWeight.setText("")
        itrComplaint.setText("")
        itrDiagnosis.setText("")
        tvSpecificDiagnosis?.setText("")
        itrTreatment.setText("")
    }

    private fun savePatientsData() {

        val fields2 = HashMap<String, Any>()
        fields2.clear()

        val vitrLName = itrLName.text.toString()
        fields2["vitrLName"] = vitrLName

        val vitrFName = itrFName.text.toString()
        fields2["vitrFName"] = vitrFName

        val vitrMName = itrMName.text.toString()
        fields2["vitrMName"] = vitrMName

        val vitrSuffix = itrSuffix.text.toString()
        fields2["vitrSuffix"] = vitrSuffix

        val vitrAge = itrAge.text.toString()
        fields2["vitrAge"] = vitrAge

        val vitrSex = itrSex.text.toString()
        fields2["vitrSex"] = vitrSex

        val space = ", "
        val lastName: String = itrLName.getText().toString()
        val firstName: String = itrFName.getText().toString()
        val fullName = concat(lastName, space ,firstName)
        val vitrFullName = fullName
        fields2["vitrFullName"] = vitrFullName

        val vitrResAdd = itrAddress.text.toString()
        fields2["vitrResAdd"] = vitrResAdd

        val vitrModeOfTransac = getMode
        fields2["vitrModeOfTransac"] = vitrModeOfTransac

        val vitrDateOfConsult = itrDateOfConsult.text.toString()
        fields2["vitrDateOfConsult"] = vitrDateOfConsult

        val vitrConsultTime = itrConsultTime.text.toString()
        fields2["vitrConsultTime"] = vitrConsultTime

        val vitrBP = itrBP.text.toString()
        fields2["vitrBP"] = vitrBP

        val vitrTemp = itrTemp.text.toString()
        fields2["vitrTemp"] = vitrTemp

        val vitrHeight = itrHeight.text.toString()
        fields2["vitrHeight"] = vitrHeight

        val vitrWeight = itrWeight.text.toString()
        fields2["vitrWeight"] = vitrWeight

        val vitrTypeOfConsult = getType
        fields2["vitrTypeOfConsult"] = vitrTypeOfConsult

        val vitrComplaint = itrComplaint.text.toString()
        fields2["vitrComplaint"] = vitrComplaint

        val vitrDiagnosis = itrDiagnosis.text.toString()
        fields2["vitrDiagnosis"] = vitrDiagnosis

        var vitrSpecificDiagnosis = ""

        if(tvSpecificDiagnosis?.text.toString() == "Others"){

            val fields3 = HashMap<String, Any>()
            fields3.clear()

            vitrSpecificDiagnosis = temptvSpecificDiagnosis?.text.toString()

            fields3["vitrSpecificDiagnosis"] = vitrSpecificDiagnosis

            val specificID = dbRef.push().key!!
            FirebaseDatabase.getInstance().getReference("SpecificDiagnosis/$specificID").updateChildren(fields3)

        }else{

            vitrSpecificDiagnosis = tvSpecificDiagnosis?.text.toString()
        }

        fields2["vitrSpecificDiagnosis"] = vitrSpecificDiagnosis

        val vitrTreatment = itrTreatment.text.toString()
        fields2["vitrTreatment"] = vitrTreatment

        val vitrDateOfReturn = itrDateOfReturn.text.toString()
        fields2["vitrDateOfReturn"] = vitrDateOfReturn

        val perId = perId.text.toString()
        fields2["pxKey"] = perId

        val vitrId = dbRef.push().key!!

        val userKey1 = Global.userKey.toString()
        fields2["userKey1"] = userKey1

        val userKey2 = ""
        fields2["userKey2"] = userKey2

        val zDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        fields2["zdateTime"] = zDateTime

//        val zUpDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
//        fields2["zupDateTime"] = zUpDateTime

        if(vitrLName.isEmpty()){
            itrLName.error = "Please enter last name"
        }
        if(vitrFName.isEmpty()){
            itrFName.error = "Please enter first name"
        }
        if(vitrMName.isEmpty()){
            itrMName.error = "Please enter middle name"
        }
        if(vitrResAdd.isEmpty()){
            itrAddress.error = "Please enter address"
        }
        if(vitrBP.isEmpty()){
            itrBP.error = "Please enter blood pressure"
        }
        if(vitrTemp.isEmpty()){
            itrTemp.error = "Please enter temperature"
        }
        if(vitrHeight.isEmpty()){
            itrHeight.error = "Please enter height"
        }
        if(vitrWeight.isEmpty()){
            itrWeight.error = "Please enter weight"
        }
        if(vitrComplaint.isEmpty()){
            itrComplaint.error = "Please enter complaint"
        }
        if(vitrDiagnosis.isEmpty()){
            itrDiagnosis.error = "Please enter diagnosis"
        }
        if(vitrSpecificDiagnosis.isEmpty()){
            tvSpecificDiagnosis?.error = "Please enter specific diagnosis"
        }
        if(vitrTreatment.isEmpty()){
            itrTreatment.error = "Please enter treatment"

        }else{

            val repId = dbRef.push().key!!

            FirebaseDatabase.getInstance().getReference("MainConsultations/$perId/$vitrId").updateChildren(fields2)

            val fields1 = HashMap<String, Any>()
            fields1.clear()
            fields1["MainConsultationsKey"] = vitrId
            fields1["MainPatientKey"] = perId
            fields1["vitrDateOfConsult"] = vitrDateOfConsult
            fields1["vitrConsultTime"] = vitrConsultTime
            fields1["vitrSpecificDiagnosis"] = vitrSpecificDiagnosis
            fields1["vitrModeOfTransac"] = vitrModeOfTransac


            FirebaseDatabase.getInstance().getReference("ReportConsultations/$repId").updateChildren(fields1)

            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()

            clearInput()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }
}