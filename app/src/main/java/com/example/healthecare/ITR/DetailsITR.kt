package com.example.healthecare.ITR

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.healthecare.global.Global
import com.example.healthecare.QR.QRGenerate
import com.example.healthecare.R
import com.example.healthecare.activities.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class DetailsITR : AppCompatActivity() {

    private lateinit var tvItrId: TextView
    private lateinit var tvPatLName: TextView
    private lateinit var tvPatFName: TextView
    private lateinit var tvPatMName: TextView
    private lateinit var tvPatSuffix: TextView
    private lateinit var tvPatFullName: TextView
    private lateinit var tvPatAddress: TextView
    private lateinit var tvPatModeOfTransac: TextView
    private lateinit var tvPatSpeDiagnosis: TextView
    private lateinit var tvPatDate: TextView
    private lateinit var tvPatReturnDate: TextView
    private lateinit var tvPatTime: TextView
    private lateinit var tvPatBP: TextView
    private lateinit var tvPatTemp: TextView
    private lateinit var tvPatHeight: TextView
    private lateinit var tvPatWeight: TextView
    private lateinit var tvPatTypeOfConsult: TextView
    private lateinit var tvPatComplaint: TextView
    private lateinit var tvPatDiagnosis: TextView

    private lateinit var userKey1: TextView
    private lateinit var userKey2: TextView
    private lateinit var zdateTime: TextView
    private lateinit var zupDateTime: TextView

    private lateinit var tvPatTreatment: TextView
    private lateinit var btnUpdate: Button
//    private lateinit var btnDelete: Button
    private lateinit var btnQRGenerate: Button

    private lateinit var mainConsultationsKey: String
    private lateinit var mainPatientKey: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_itr)

        supportActionBar!!.title = "Individual Treatment Details"

        mainPatientKey = Global.mainPatientKey
        mainConsultationsKey = Global.mainConsultationsKey

        initView()
        setValuesToViews(mainConsultationsKey, mainPatientKey)

        btnUpdate.setOnClickListener {
//            openUpdate()
            openConfirm()
        }

//        btnDelete.setOnClickListener {
//            showDeleteDialog()
//        }

        btnQRGenerate.setOnClickListener {
            val intent = Intent(this, QRGenerate::class.java)
            startActivity(intent)
        }
    }

    private fun openConfirm() {
        val view: View = LayoutInflater.from(this@DetailsITR).inflate(R.layout.layout_confirm_update, null)
        val builder = AlertDialog.Builder(this@DetailsITR, R.style.AlertDialogTheme).setView(view)
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

        val view: View = LayoutInflater.from(this@DetailsITR).inflate(R.layout.layout_dialog_delete, null)
        val builder = AlertDialog.Builder(this@DetailsITR, R.style.AlertDialogTheme).setView(view)
        builder.setView(view)

        (view.findViewById<View>(R.id.textTitle) as TextView).text =
            resources.getString(R.string.confirm_delete)
        (view.findViewById<View>(R.id.textMessage) as TextView).text =
            resources.getString(R.string.confirmation_delete)

        val alertDialog = builder.create()

        view.findViewById<View>(R.id.btnDelete).setOnClickListener {

            deleteRecord(mainPatientKey, mainConsultationsKey)

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

    private fun showError() {
        val view: View = LayoutInflater.from(this@DetailsITR)
            .inflate(R.layout.layout_dialog_error, null)
        val builder = AlertDialog.Builder(this@DetailsITR, R.style.AlertDialogTheme).setView(view)
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

    private fun deleteRecord(mainPatientKey: String, mainConsultationsKey: String) {
        val dbRef = FirebaseDatabase.getInstance().reference.child("MainConsultations/$mainPatientKey/$mainConsultationsKey")
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Consultation data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)

        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        tvItrId = findViewById(R.id.tvItrId)
        tvPatLName = findViewById(R.id.tvItrLName)
        tvPatFName = findViewById(R.id.tvItrFName)
        tvPatMName = findViewById(R.id.tvItrMName)
        tvPatSuffix = findViewById(R.id.tvItrSuffix)
        tvPatFullName = findViewById(R.id.tvItrFullName)
        tvPatAddress = findViewById(R.id.tvItrAddress)
        tvPatModeOfTransac = findViewById(R.id.tvItrModeOfTransaction)
        tvPatDate = findViewById(R.id.tvItrDate)
        tvPatTime = findViewById(R.id.tvItrTime)
        tvPatReturnDate = findViewById(R.id.tvItrReturnDate)
        tvPatBP = findViewById(R.id.tvItrBP)
        tvPatTemp = findViewById(R.id.tvItrTemp)
        tvPatHeight = findViewById(R.id.tvItrHeight)
        tvPatWeight = findViewById(R.id.tvItrWeight)
        tvPatTypeOfConsult = findViewById(R.id.tvItrTypeOfConsultation)
        tvPatComplaint = findViewById(R.id.tvItrComplaint)
        tvPatDiagnosis = findViewById(R.id.tvItrDiagnosis)
        tvPatSpeDiagnosis = findViewById(R.id.tvItrSpeDiagnosis)
        tvPatTreatment = findViewById(R.id.tvItrTreatment)
        btnUpdate = findViewById(R.id.btnUpdate)
//        btnDelete = findViewById(R.id.btnDelete)
        btnQRGenerate = findViewById(R.id.btnQRGenerate)

        userKey1 = findViewById(R.id.userKey1)
        userKey2 = findViewById(R.id.userKey2)
        zdateTime = findViewById(R.id.zdateTime)
        zupDateTime = findViewById(R.id.zupDateTime)
    }

    private fun setValuesToViews(mainConsultationsKey: String, mainPatientKey: String) {

        var dbRef =
            FirebaseDatabase.getInstance().reference.child("MainPatient/$mainPatientKey").get()
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        val snapshot = task.result
                        val vitrLName = snapshot.child("vperLName").value.toString()
                        val vitrFName = snapshot.child("vperFName").value.toString()
                        val vitrMName = snapshot.child("vperMName").value.toString()
                        val vitrSuffix = snapshot.child("vperSuffix").value.toString()
                        val vitrAddress = snapshot.child("vperResAdd").value.toString()

                        tvPatLName.text = vitrLName
                        tvPatFName.text = vitrFName
                        tvPatMName.text = vitrMName
                        tvPatSuffix.text = vitrSuffix
                        tvPatAddress.text = vitrAddress

                        Global.vitrFullName = "$vitrLName, $vitrFName"


                    } else {
                        showError()
                    }

                }.addOnFailureListener {
                showError()
            }

        dbRef =
            FirebaseDatabase.getInstance().reference.child("MainConsultations/$mainPatientKey/$mainConsultationsKey")
                .get().addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val snapshot = task.result
                    val vitrModeOfTransac = snapshot.child("vitrModeOfTransac").value.toString()
                    val vitrDateOfConsult = snapshot.child("vitrDateOfConsult").value.toString()
                    val vitrConsultTime = snapshot.child("vitrConsultTime").value.toString()
                    val vitrBP = snapshot.child("vitrBP").value.toString()
                    val vitrTemp = snapshot.child("vitrTemp").value.toString()
                    val vitrHeight = snapshot.child("vitrHeight").value.toString()
                    val vitrWeight = snapshot.child("vitrWeight").value.toString()
                    val vitrTypeOfConsult = snapshot.child("vitrTypeOfConsult").value.toString()
                    val vitrComplaint = snapshot.child("vitrComplaint").value.toString()
                    val vitrDiagnosis = snapshot.child("vitrDiagnosis").value.toString()
                    val vitrSpecificDiagnosis = snapshot.child("vitrSpecificDiagnosis").value.toString()
                    val vitrTreatment = snapshot.child("vitrTreatment").value.toString()
                    val vitrDateOfReturn = snapshot.child("vitrDateOfReturn").value.toString()

                    var userKey1x = ""
                    if(snapshot.child("userKey1").exists()) {
                        userKey1x = snapshot.child("userKey1").value.toString()
                    }

                    var userKey2x = ""
                    if(snapshot.child("userKey2").exists()) {
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

                    Global.vitrDateOfReturn = vitrDateOfReturn

                    tvItrId.text = mainConsultationsKey
                    tvPatModeOfTransac.text = vitrModeOfTransac

                    val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Manila"))

                    tvPatDate.text = vitrDateOfConsult
                    tvPatTime.text = vitrConsultTime
                    tvPatBP.text = vitrBP
                    tvPatTemp.text = vitrTemp
                    tvPatHeight.text = vitrHeight
                    tvPatWeight.text = vitrWeight
                    tvPatTypeOfConsult.text = vitrTypeOfConsult
                    tvPatComplaint.text = vitrComplaint
                    tvPatDiagnosis.text = vitrDiagnosis
                    tvPatSpeDiagnosis.text = vitrSpecificDiagnosis
                    tvPatTreatment.text = vitrTreatment
                    tvPatReturnDate.text = vitrDateOfReturn

                    if(vitrDateOfReturn == ""){
                        btnQRGenerate.isEnabled = false
                    }else{
                        btnQRGenerate.isEnabled = true
                    }

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

    private fun openUpdate() {
        val intent = Intent(this, PatientsDetailsActivityITR::class.java)
        startActivity(intent)
        this.finish()
    }
}