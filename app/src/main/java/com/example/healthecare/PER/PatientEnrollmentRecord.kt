package com.example.healthecare.PER

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.healthecare.R
import com.example.healthecare.activities.MainActivity
import com.example.healthecare.global.Global
import com.example.healthecare.models.PERModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class PatientEnrollmentRecord : AppCompatActivity() {

    private lateinit var perLName: EditText
    private lateinit var perMName: EditText
    private lateinit var perFName: EditText
    private lateinit var perSuffix: EditText
    private lateinit var perAge: EditText
    private lateinit var perSex: Spinner
    private lateinit var perBirthdate: TextView
    private lateinit var perBirthplace: EditText
    private lateinit var perCivilStatus: Spinner
    private lateinit var perSpouse: EditText
    private lateinit var perEducAttain: Spinner
    private lateinit var perEmpStatus: Spinner
    private lateinit var perFamMem: Spinner
    private lateinit var perMomName: EditText
    private lateinit var perMomBirth: TextView
    private lateinit var perResAdd: EditText
    private lateinit var perDadName: EditText
    private lateinit var perDadBirth: TextView
    private lateinit var perContactNum: EditText

    private lateinit var rg4psMem: RadioGroup
    private lateinit var per4psMem: TextView

    private lateinit var rgPhilHealthMem: RadioGroup
    private lateinit var perPhilHealthMem: TextView

    private lateinit var rgStatusType: RadioGroup
    private lateinit var perStatusType: TextView

    private lateinit var perPhilHealthNo: EditText

    private lateinit var llStatus: LinearLayout
    private lateinit var llPhilHealthNo: LinearLayout

    private lateinit var perBrgy: Spinner

    private lateinit var getCivil : String
    private lateinit var getEduc : String
    private lateinit var getEmp : String
    private lateinit var getFam : String
    private lateinit var getBrgy : String
    private lateinit var getGender : String

    private lateinit var btnSave: Button
    private lateinit var btnDate: Button
    private lateinit var btnDate1: Button
    private lateinit var btnDate2: Button
    private lateinit var dbRef: DatabaseReference

//    private lateinit var pos: TextView

    private var isListItemClicked = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.per_form)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Patient Enrollment Record Form"

        val date = Calendar.getInstance()
        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH)
        val day = date.get(Calendar.DAY_OF_MONTH)

        val date1 = Calendar.getInstance()
        val year1 = date1.get(Calendar.YEAR)
        val month1 = date1.get(Calendar.MONTH)
        val day1 = date1.get(Calendar.DAY_OF_MONTH)

        val date2 = Calendar.getInstance()
        val year2 = date2.get(Calendar.YEAR)
        val month2 = date2.get(Calendar.MONTH)
        val day2 = date2.get(Calendar.DAY_OF_MONTH)

        initView()
        civilStatus()
        educAttain()
        empStatus()
        famMem()
        brgy()
        gender()

        btnSave.setOnClickListener {
            if(isListItemClicked) {
                saveResidentsData()
            }
        }

        btnDate.setOnClickListener {
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->

                perBirthdate.setText("" + mYear + "-" + (mMonth + 1) + "-" + mDay)

                val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Manila"))
                cal.time = SimpleDateFormat("yyyy-MM-dd").parse("" + mYear + "-" + (mMonth + 1) + "-" + mDay)

                //zdateTime.text = SimpleDateFormat("MMMM dd, yyyy hh:mm aa").format(cal.time).toString().uppercase()

                val currentDate = Calendar.getInstance()

                val myFormat = SimpleDateFormat("yyyy-MM-dd")

                var birthdate: Date? = null

                try {
                    birthdate = myFormat.parse("" + mYear + "-" + (mMonth + 1) + "-" + mDay)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                val time = currentDate.time.time / 1000 - birthdate!!.time / 1000

                val years = Math.round(time.toFloat()) / 31536000
                //val months = Math.round((time - years * 31536000).toFloat()) / 2628000

                perAge.setText("" + years)

            }, year, month, day)
            datePicker.show()
        }

        btnDate1.setOnClickListener {
            val datePicker1 = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker1, mYear, mMonth, mDay ->
                perMomBirth.setText("" + mYear + "-" + (mMonth + 1) + "-" + mDay)
            }, year1, month1, day1)
            datePicker1.show()
        }

        btnDate2.setOnClickListener {
            val datePicker2 = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker2, mYear, mMonth, mDay ->
                perDadBirth.setText("" + mYear + "-" + (mMonth + 1) + "-" + mDay)
            }, year2, month2, day2)
            datePicker2.show()
        }

        rgPhilHealthMem.setOnCheckedChangeListener { rgPhilHealthMem, checkedId ->

            llStatus.isVisible = R.id.rbPhilHealthMem1 == checkedId
            llPhilHealthNo.isInvisible = R.id.rbPhilHealthMem2 == checkedId

            val rb = findViewById<RadioButton>(checkedId)
            if(rb!=null)
                perPhilHealthMem.setText(rb.text.toString())
        }

        rgStatusType.setOnCheckedChangeListener { rgStatusType, checkedId ->

            llPhilHealthNo.isInvisible = R.id.rbStatus1 == checkedId
            llPhilHealthNo.isVisible = R.id.rbStatus2 == checkedId

            val rb = findViewById<RadioButton>(checkedId)
            if(rb!=null)
                perStatusType.setText(rb.text.toString())
        }

        rg4psMem.setOnCheckedChangeListener { rg4psMem, checkedId ->
            val rb = findViewById<RadioButton>(checkedId)
            if(rb!=null)
                per4psMem.setText(rb.text.toString())
        }
    }

    private fun initView() {

        perLName = findViewById(R.id.perLName)
        perFName = findViewById(R.id.perFName)
        perMName = findViewById(R.id.perMName)
        perSuffix = findViewById(R.id.perSuffix)
        perAge = findViewById(R.id.perAge)
        perSex = findViewById(R.id.perSex)
        perBirthdate = findViewById(R.id.perBirthdate)
        perBirthplace = findViewById(R.id.perBirthplace)
        perCivilStatus = findViewById(R.id.perCivilStatus)
        perSpouse = findViewById(R.id.perSpouse)
        perEducAttain = findViewById(R.id.perEduc)
        perEmpStatus = findViewById(R.id.perEmpStatus)
        perFamMem = findViewById(R.id.perFamMem)
        perMomName = findViewById(R.id.perMotherName)
        perMomBirth = findViewById(R.id.perMotherBirth)
        perResAdd = findViewById(R.id.perResidentialAdd)
        perDadName = findViewById(R.id.perFatherName)
        perDadBirth = findViewById(R.id.perFatherBirth)
        perContactNum = findViewById(R.id.perContact)

        rg4psMem = findViewById(R.id.rg4psMem)
        per4psMem = findViewById(R.id.per4psMem)

        rgPhilHealthMem = findViewById(R.id.rgPhilHealthMem)
        perPhilHealthMem = findViewById(R.id.perPhilHealthMem)

        rgStatusType = findViewById(R.id.rgStatusType)
        perStatusType = findViewById(R.id.perStatusType)

        perPhilHealthNo = findViewById(R.id.perPhilHealthNo)
        llStatus = findViewById(R.id.llStatus)
        llPhilHealthNo = findViewById(R.id.llPhilHealthNo)

        perBrgy = findViewById(R.id.perBrgy)

        btnSave = findViewById(R.id.btnSave)
        btnDate = findViewById(R.id.btnDate)
        btnDate1 = findViewById(R.id.btnDate1)
        btnDate2 = findViewById(R.id.btnDate2)
        dbRef = FirebaseDatabase.getInstance().getReference("MainPatient")

//        pos = findViewById(R.id.pos)
    }

    private fun concat(lastName: String, space: String, firstName: String): String{
        return lastName + space + firstName
    }

    private fun civilStatus() {
        perCivilStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                perCivilStatus.onItemSelectedListener = this
                getCivil = adapterView?.getItemAtPosition(position).toString()
            }
        }
    }

    private fun educAttain() {
        perEducAttain.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                perEducAttain.onItemSelectedListener = this
                getEduc = adapterView?.getItemAtPosition(position).toString()
            }
        }
    }

    private fun empStatus() {
        perEmpStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                perEmpStatus.onItemSelectedListener = this
                getEmp = adapterView?.getItemAtPosition(position).toString()
            }
        }
    }

    private fun famMem() {
        perFamMem.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                perFamMem.onItemSelectedListener = this
                getFam = adapterView?.getItemAtPosition(position).toString()
            }
        }
    }

    private fun brgy() {
        perBrgy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                perBrgy.onItemSelectedListener = this
                getBrgy = adapterView?.getItemAtPosition(position).toString()
            }
        }
    }

    private fun gender() {
        perSex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {

                perSex.onItemSelectedListener = this
                getGender = adapterView?.getItemAtPosition(position).toString()
            }
        }
    }


    private fun clearInput() {
        perLName.setText("")
        perFName.setText("")
        perMName.setText("")
        perSuffix.setText("")
        perAge.setText("")
        perBirthdate.setText("")
        perBirthplace.setText("")
        perSpouse.setText("")
        perMomName.setText("")
        perMomBirth.setText("")
        perResAdd.setText("")
        perDadName.setText("")
        perDadBirth.setText("")
        perContactNum.setText("")
        perPhilHealthNo.setText("")
    }


    private fun saveResidentsData() {

        val fields1 = HashMap<String, Any>()
        fields1.clear()

        val vperLName = perLName.text.toString()
        fields1["vperLName"] = vperLName

        val vperFName = perFName.text.toString()
        fields1["vperFName"] = vperFName

        val vperMName = perMName.text.toString()
        fields1["vperMName"] = vperMName

        val space = ", "
        val lastName: String = perLName.getText().toString()
        val firstName: String = perFName.getText().toString()
        val fullName = concat(lastName, space ,firstName)
        val vperFullName = fullName
        fields1["vperFullName"] = vperFullName

        val vperSuffix = perSuffix.text.toString()
        fields1["vperSuffix"] = vperSuffix

        val vperAge = perAge.text.toString()
        fields1["vperAge"] = vperAge

        val vperSex = getGender
        fields1["vperSex"] = vperSex

        val vperBirthdate = perBirthdate.text.toString()
        fields1["vperBirthdate"] = vperBirthdate

        val vperBirthplace = perBirthplace.text.toString()
        fields1["vperBirthplace"] = vperBirthplace

        val vperCivilStatus = getCivil
        fields1["vperCivilStatus"] = vperCivilStatus

        val vperSpouse = perSpouse.text.toString()
        fields1["vperSpouse"] = vperSpouse

        val vperEducAttain = getEduc
        fields1["vperEducAttain"] = vperEducAttain

        val vperEmpStatus = getEmp
        fields1["vperEmpStatus"] = vperEmpStatus

        val vperFamMem = getFam
        fields1["vperFamMem"] = vperFamMem

        val vperMomName = perMomName.text.toString()
        fields1["vperMomName"] = vperMomName

        val vperMomBirth = perMomBirth.text.toString()
        fields1["vperMomBirth"] = vperMomBirth

        val vperResAdd = perResAdd.text.toString()
        fields1["vperResAdd"] = vperResAdd

        val vperDadName = perDadName.text.toString()
        fields1["vperDadName"] = vperDadName

        val vperDadBirth = perDadBirth.text.toString()
        fields1["vperDadBirth"] = vperDadBirth

        val vperContactNum = perContactNum.text.toString()
        fields1["vperContactNum"] = vperContactNum

        val vper4psMem = per4psMem.text.toString()
        fields1["vper4psMem"] = vper4psMem

        val vperPhilHealthMem = perPhilHealthMem.text.toString()
        fields1["vperPhilHealthMem"] = vperPhilHealthMem

        val vperStatusType = perStatusType.text.toString()
        fields1["vperStatusType"] = vperStatusType

        val vperPhilHealthNo = perPhilHealthNo.text.toString()
        fields1["vperPhilHealthNo"] = vperPhilHealthNo

        val vperBrgy = getBrgy
        fields1["vperBrgy"] = vperBrgy

        val userKey1 = Global.userKey.toString()
        fields1["userKey1"] = userKey1

        val userKey2 = ""
        fields1["userKey2"] = userKey2

        val zdateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        fields1["zdateTime"] = zdateTime

//        val zUpDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
//        fields1["zUpDateTime"] = zUpDateTime

        val key = dbRef.push().key!!

        if(vperLName.isEmpty()){
            perLName.error = "Please enter your last name"
        }
        if(vperFName.isEmpty()){
            perFName.error = "Please enter your first name"
        }
        if(vperMName.isEmpty()){
            perMName.error = "Please enter your middle name"
        }
        if(vperAge.isEmpty()){
            perAge.error = "Please enter your age"
        }

        rgStatusType.setOnCheckedChangeListener { rgStatusType, checkedId ->
            llPhilHealthNo.isVisible = R.id.rbStatus2 == checkedId
            if(vperPhilHealthNo.isEmpty()){
                perPhilHealthNo.error = "Please enter your PhilHealth No."
            }
        }

        if(vperBirthplace.isEmpty()){
            perBirthplace.error = "Please enter your birthplace"
        }
        if(vperResAdd.isEmpty()){
            perResAdd.error = "Please enter your address"
        }
        if(vperContactNum.isEmpty()){
            perContactNum.error = "Please enter your contact number"

        } else {

            FirebaseDatabase.getInstance().getReference("MainPatient/$key").updateChildren(fields1)

            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()

            clearInput()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
