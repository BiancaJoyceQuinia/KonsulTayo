package com.example.healthecare.PER

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.healthecare.ITR.FetchingActivityITR
import com.example.healthecare.ITR.IndividualTreatmentRecord
import com.example.healthecare.R
import com.example.healthecare.activities.MainActivity
import com.example.healthecare.databinding.ActivityDetailsPerBinding
import com.example.healthecare.global.Global
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


class DetailsPER: AppCompatActivity() {

    private lateinit var binding: ActivityDetailsPerBinding
    private lateinit var tvPatId: TextView
    private lateinit var tvPatLName: TextView
    private lateinit var tvPatFName: TextView
    private lateinit var tvPatMName: TextView
    private lateinit var tvPatSuffix: TextView
    private lateinit var tvPatFullName: TextView
    private lateinit var tvPatAge: TextView
    private lateinit var tvPatSex: TextView
    private lateinit var tvPatBirthdate: TextView
    private lateinit var tvPatBirthplace: TextView
    private lateinit var tvPatCivilStatus: TextView
    private lateinit var tvPatSpouse: TextView
    private lateinit var tvPatEducAttain: TextView
    private lateinit var tvPatEmpStatus: TextView
    private lateinit var tvPatFamMem: TextView
    private lateinit var tvPatMomName: TextView
    private lateinit var tvPatMomBirth: TextView
    private lateinit var tvPatResAdd: TextView
    private lateinit var tvPatBrgy: TextView
    private lateinit var tvPatDadName: TextView
    private lateinit var tvPatDadBirth: TextView
    private lateinit var tvPatContactNum: TextView
    private lateinit var tvPat4psMem: TextView
    private lateinit var tvPatPhilHealthMem: TextView
    private lateinit var tvPatStatusType: TextView
    private lateinit var tvPatPhilHealthNo: TextView

    private lateinit var userKey1: TextView
    private lateinit var userKey2: TextView
    private lateinit var zdateTime: TextView
    private lateinit var zupDateTime: TextView

    private lateinit var btnUpdate: Button
//    private lateinit var btnDelete: Button
    private lateinit var btnITR: LinearLayout
    private lateinit var btnConsultations: LinearLayout

    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsPerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Patient Enrollment Details"

        key = Global.key

        initView()
        setValuesToViews(key)
        sendData()

        btnUpdate.setOnClickListener {
//            openUpdate()
            openConfirm()
        }

//        btnDelete.setOnClickListener {
//            showDeleteDialog()
//        }

        btnConsultations.setOnClickListener {
            val key = intent.getStringExtra("key").toString()
            val intent = Intent(this, FetchingActivityITR::class.java)
            intent.putExtra("key",  key)
            startActivity(intent)
        }
    }

    private fun openConfirm() {
        val view: View = LayoutInflater.from(this@DetailsPER).inflate(R.layout.layout_confirm_update, null)
        val builder = AlertDialog.Builder(this@DetailsPER, R.style.AlertDialogTheme).setView(view)
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
                                            openUpdate()
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


    private fun showDeleteDialog() {

        val view: View = LayoutInflater.from(this@DetailsPER).inflate(R.layout.layout_dialog_delete, null)
        val builder = AlertDialog.Builder(this@DetailsPER, R.style.AlertDialogTheme).setView(view)
        builder.setView(view)

        (view.findViewById<View>(R.id.textTitle) as TextView).text =
            resources.getString(R.string.confirm_delete)
        (view.findViewById<View>(R.id.textMessage) as TextView).text =
            resources.getString(R.string.confirmation_delete)

        val alertDialog = builder.create()

        view.findViewById<View>(R.id.btnDelete).setOnClickListener {

            deleteRecord(key)

            alertDialog.dismiss()
        }

        view.findViewById<View>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }

    private fun sendData() {
        btnITR.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("key", binding.tvPerId.text.toString())
            bundle.putString("vperLName", binding.tvPerLName.text.toString())
            bundle.putString("vperFName", binding.tvPerFName.text.toString())
            bundle.putString("vperMName", binding.tvPerMName.text.toString())
            bundle.putString("vperSuffix", binding.tvPerSuffix.text.toString())
            bundle.putString("vperResAdd", binding.tvPerResAdd.text.toString())
            bundle.putString("vperAge", binding.tvPerAge.text.toString())
            bundle.putString("vperSex", binding.tvPerSex.text.toString())

            val intent = Intent(this, IndividualTreatmentRecord::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun showError() {
        val view: View = LayoutInflater.from(this@DetailsPER)
            .inflate(R.layout.layout_dialog_error, null)
        val builder = AlertDialog.Builder(this@DetailsPER, R.style.AlertDialogTheme).setView(view)
        builder.setView(view)

        (view.findViewById<View>(R.id.textTitle) as TextView).text =
            resources.getString(R.string.confirm_error)
        (view.findViewById<View>(R.id.textMessage) as TextView).text =
            resources.getString(R.string.confirmation_error)

        val alertDialog = builder.create()

        view.findViewById<View>(R.id.btnOkay).setOnClickListener {
            alertDialog.dismiss()
        }

        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        alertDialog.show()
    }

    private fun deleteRecord(key: String){
        val dbRef = FirebaseDatabase.getInstance().getReference("MainPatient").child(key)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Patient data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivityPER::class.java)
            finish()
            startActivity(intent)

        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Error ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {

        tvPatId = findViewById(R.id.tvPerId)
        tvPatLName = findViewById(R.id.tvPerLName)
        tvPatFName = findViewById(R.id.tvPerFName)
        tvPatMName = findViewById(R.id.tvPerMName)
        tvPatSuffix = findViewById(R.id.tvPerSuffix)
        tvPatFullName = findViewById(R.id.tvPerFullName)
        tvPatAge = findViewById(R.id.tvPerAge)
        tvPatSex = findViewById(R.id.tvPerSex)
        tvPatBirthdate = findViewById(R.id.tvPerBirthdate)
        tvPatBirthplace = findViewById(R.id.tvPerBirthplace)
        tvPatCivilStatus = findViewById(R.id.tvPerCivilStatus)
        tvPatSpouse = findViewById(R.id.tvPerSpouse)
        tvPatEducAttain = findViewById(R.id.tvPerEducAttain)
        tvPatEmpStatus = findViewById(R.id.tvPerEmpStats)
        tvPatFamMem = findViewById(R.id.tvPerFamMem)
        tvPatMomName = findViewById(R.id.tvPerMomName)
        tvPatMomBirth = findViewById(R.id.tvPerMomBirth)
        tvPatResAdd = findViewById(R.id.tvPerResAdd)
        tvPatBrgy = findViewById(R.id.tvPerBrgy)
        tvPatDadName = findViewById(R.id.tvPerDadName)
        tvPatDadBirth = findViewById(R.id.tvPerDadBirth)
        tvPatContactNum = findViewById(R.id.tvPerContactNum)
        tvPat4psMem = findViewById(R.id.tvPer4psMem)
        tvPatPhilHealthMem = findViewById(R.id.tvPerPhilMem)
        tvPatStatusType = findViewById(R.id.tvPerStatusType)
        tvPatPhilHealthNo = findViewById(R.id.tvPerPhilHealthNo)

        userKey1 = findViewById(R.id.userKey1)
        userKey2 = findViewById(R.id.userKey2)
        zdateTime = findViewById(R.id.zdateTime)
        zupDateTime = findViewById(R.id.zupDateTime)

//        tvPos = findViewById(R.id.tvPos)

        btnUpdate = findViewById(R.id.btnUpdate)
//        btnDelete = findViewById(R.id.btnDelete)
        btnITR = findViewById(R.id.btnITR)
        btnConsultations = findViewById(R.id.btnConsultations)
    }

    private fun setValuesToViews(key: String) {

        var dbRef =
            FirebaseDatabase.getInstance().reference.child("MainPatient/$key")
                .get().addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val snapshot = task.result
                        val vperLName = snapshot.child("vperLName").value.toString()
                        val vperFName = snapshot.child("vperFName").value.toString()
                        val vperMName = snapshot.child("vperMName").value.toString()
                        val vperSuffix = snapshot.child("vperSuffix").value.toString()
                        val vperFullName = snapshot.child("vperFullName").value.toString()
                        val vperAge = snapshot.child("vperAge").value.toString()
                        val vperSex = snapshot.child("vperSex").value.toString()
                        val vperBirthdate = snapshot.child("vperBirthdate").value.toString()
                        val vperBirthplace = snapshot.child("vperBirthplace").value.toString()
                        val vperCivilStatus = snapshot.child("vperCivilStatus").value.toString()
                        val vperSpouse = snapshot.child("vperSpouse").value.toString()
                        val vperEducAttain = snapshot.child("vperEducAttain").value.toString()
                        val vperEmpStatus = snapshot.child("vperEmpStatus").value.toString()
                        val vperFamMem = snapshot.child("vperFamMem").value.toString()
                        val vperMomName = snapshot.child("vperMomName").value.toString()
                        val vperMomBirth = snapshot.child("vperMomBirth").value.toString()
                        val vperResAdd = snapshot.child("vperResAdd").value.toString()
                        val vperBrgy = snapshot.child("vperBrgy").value.toString()
                        val vperDadName = snapshot.child("vperDadName").value.toString()
                        val vperDadBirth = snapshot.child("vperDadBirth").value.toString()
                        val vperContactNum = snapshot.child("vperContactNum").value.toString()
                        val vper4psMem = snapshot.child("vper4psMem").value.toString()
                        val vperPhilHealthMem = snapshot.child("vperPhilHealthMem").value.toString()
                        val vperStatusType = snapshot.child("vperStatusType").value.toString()
                        val vperPhilHealthNo = snapshot.child("vperPhilHealthNo").value.toString()

                        var userKey1x = ""
                        if(snapshot.child("userKey1").exists()) {
                            userKey1x = snapshot.child("userKey1").value.toString()
                        }

                        var userKey2x = ""
                        if(snapshot.child("userKey2").exists()){
                            userKey2x = snapshot.child("userKey2").value.toString()
                        }

                        var zdateTimex = ""
                        if(snapshot.child("zdateTime").exists()){
                            zdateTimex = snapshot.child("zdateTime").value.toString()
                        }

                        var zupDateTimex = ""
                        if(snapshot.child("zupDateTime").exists()){
                            zupDateTimex = snapshot.child("zupDateTime").value.toString()
                        }

                        tvPatId.text = key
                        tvPatLName.text = vperLName
                        tvPatFName.text = vperFName
                        tvPatMName.text = vperMName
                        tvPatSuffix.text = vperSuffix
                        tvPatFullName.text = vperFullName
                        tvPatAge.text = vperAge
                        tvPatSex.text = vperSex

                        val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Manila"))

                        tvPatBirthdate.text = vperBirthdate

                        tvPatBirthplace.text = vperBirthplace
                        tvPatCivilStatus.text = vperCivilStatus
                        tvPatSpouse.text = vperSpouse
                        tvPatEducAttain.text = vperEducAttain
                        tvPatEmpStatus.text = vperEmpStatus
                        tvPatFamMem.text = vperFamMem
                        tvPatMomName.text = vperMomName
                        tvPatMomBirth.text = vperMomBirth
                        tvPatResAdd.text = vperResAdd
                        tvPatBrgy.text = vperBrgy
                        tvPatDadName.text = vperDadName
                        tvPatDadBirth.text = vperDadBirth
                        tvPatContactNum.text = vperContactNum
                        tvPat4psMem.text = vper4psMem
                        tvPatPhilHealthMem.text = vperPhilHealthMem
                        tvPatStatusType.text = vperStatusType
                        tvPatPhilHealthNo.text = vperPhilHealthNo

                        FirebaseDatabase.getInstance().reference.child("Users/$userKey1x").get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val snapshot = task.result
                                val username = snapshot.child("fullname").value.toString()
                                if(username != "") {
                                    userKey1.text = username
                                }
                            } else {
                                showError()
                            }
                        }.addOnFailureListener {
                            showError()
                        }

                        if(userKey2x != ""){

                            FirebaseDatabase.getInstance().reference.child("Users/$userKey2x").get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    val snapshot = task.result
                                    val username = snapshot.child("fullname").value.toString()

                                    userKey2.text = username
                                } else {
                                    showError()
                                }
                            }.addOnFailureListener {
                                showError()
                            }

                            if(zupDateTimex != "") {
                                cal.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(zupDateTimex.toString())
                                zupDateTime.text = SimpleDateFormat("MMMM dd, yyyy hh:mm aa").format(cal.time).toString().uppercase()
                            }

                        }else{
                            userKey2.text = "N/A"
                            zupDateTime.text = "N/A"
                        }

                        if(zdateTimex != ""){
                            cal.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(zdateTimex.toString())
                            zdateTime.text = SimpleDateFormat("MMMM dd, yyyy hh:mm aa").format(cal.time).toString().uppercase()
                        }else{
                            zdateTime.text = "N/A"
                        }

                    } else {
                        showError()
                    }

                }.addOnFailureListener {
                    showError()
                }

    }

    private fun openUpdate(){
        val intent = Intent(this, PatientsDetailsActivityPER::class.java)
        startActivity(intent)
        this.finish()
    }
}