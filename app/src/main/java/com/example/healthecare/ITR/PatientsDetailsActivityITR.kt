package com.example.healthecare.ITR

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.healthecare.global.Global
import com.example.healthecare.R
import com.example.healthecare.databinding.ActivityPatientDetailsItrBinding
import com.example.healthecare.models.SpecificDiagnosis
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PatientsDetailsActivityITR : AppCompatActivity() {

    private lateinit var mainConsultationsKey: String
    private lateinit var mainPatientKey: String
    var dialog: Dialog? = null

    val spinnerValueA = mutableListOf("")

    private lateinit var binding: ActivityPatientDetailsItrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var vitrModeOfTransacValue = ""
        var vitrTypeOfConsult = ""

        val date = Calendar.getInstance()
        var year = date.get(Calendar.YEAR)
        var month = date.get(Calendar.MONTH)
        var day = date.get(Calendar.DAY_OF_MONTH)

        val date1 = Calendar.getInstance()
        var year1 = date1.get(Calendar.YEAR)
        var month1 = date1.get(Calendar.MONTH)
        var day1 = date1.get(Calendar.DAY_OF_MONTH)

        binding = ActivityPatientDetailsItrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Individual Treatment Record Update"

        diagnosisList2()

        mainPatientKey = Global.mainPatientKey
        mainConsultationsKey = Global.mainConsultationsKey

        val dbRef = FirebaseDatabase.getInstance().reference.child("MainPatient/$mainPatientKey").get().addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val snapshot = task.result
                    val vperFName = snapshot.child("vperFName").value.toString().uppercase()
                    val vperMName = snapshot.child("vperMName").value.toString().uppercase()
                    val vperLName = snapshot.child("vperLName").value.toString().uppercase()
                    val vperResAdd = snapshot.child("vperResAdd").value.toString().uppercase()
                    val vperSuffix = snapshot.child("vperSuffix").value.toString().uppercase()

                    binding.tvItrFName.text = vperFName
                    binding.tvItrMName.text = vperMName
                    binding.tvItrLName.text = vperLName
                    binding.tvItrAddress.text = vperResAdd
                    binding.tvItrSuffix.text = vperSuffix

                    val dbRef = FirebaseDatabase.getInstance().reference.child("MainConsultations/$mainPatientKey/$mainConsultationsKey")
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

                                Global.vitrDateOfConsult = vitrDateOfConsult
                                Global.vitrDateOfReturn = vitrDateOfReturn

                                binding.tvItrDate.setText(vitrDateOfConsult)
                                binding.tvItrTime.setText(vitrConsultTime)
                                binding.tvItrBP.setText(vitrBP)
                                binding.tvItrModeOfTransaction.setSelection(resources.getStringArray(R.array.modeOfTransaction).indexOf(vitrModeOfTransac))
                                binding.tvItrTemp.setText(vitrTemp)
                                binding.tvItrHeight.setText(vitrHeight)
                                binding.tvItrWeight.setText(vitrWeight)
                                binding.tvItrTypeOfConsultation.setSelection(resources.getStringArray(R.array.typeOfConsultation).indexOf(vitrTypeOfConsult)) // aayusin pa
                                binding.tvItrComplaint.setText(vitrComplaint)
                                binding.tvItrDiagnosis.setText(vitrDiagnosis)
                                binding.tvItrTreatment.setText(vitrTreatment)
                                binding.tvItrReturnDate.setText(vitrDateOfReturn)

                                val temp = spinnerValueA.indexOf(vitrSpecificDiagnosis)
                                if(temp < 0){
                                    binding.tvItrSpeDiagnosis.text = "Others"
                                    binding.temptvSpecificDiagnosis.visibility = View.VISIBLE
                                    binding.temptvSpecificDiagnosis.setText(vitrSpecificDiagnosis)
                                }else{
                                    binding.tvItrSpeDiagnosis.text = vitrSpecificDiagnosis
                                    binding.temptvSpecificDiagnosis.visibility = View.GONE
                                    binding.temptvSpecificDiagnosis.setText("")
                                }

                            } else {
                                showError()
                            }

                        }.addOnFailureListener {
                            showError()
                        }

                } else {
                    showError()
                }

            }.addOnFailureListener {
                showError()
            }

        binding.btnDate.setOnClickListener {

            val temp = Global.vitrDateOfConsult.split("-")

            if(temp.isNotEmpty()){
                year = temp[0].toInt()
                month = temp[1].toInt() - 1
                day = temp[2].toInt()
            }

//            println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
//            println("Y-$year")
//            println("M-$month")
//            println("D-$day")

            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker1, mYear, mMonth, mDay ->
                binding.tvItrDate.setText("" + mYear + "-" + (mMonth + 1) + "-" + mDay)
            }, year, month, day)

            datePicker.show()
        }

        binding.btnDate1.setOnClickListener {

            if(Global.vitrDateOfReturn != "") {
                val temp = Global.vitrDateOfReturn.split("-")
                if (temp.isNotEmpty()) {
                    year1 = temp[0].toInt()
                    month1 = temp[1].toInt() - 1
                    day1 = temp[2].toInt()
                }
            }

            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker1, mYear, mMonth, mDay ->
                binding.tvItrReturnDate.setText("" + mYear + "-" + (mMonth + 1) + "-" + mDay)
            }, year1, month1, day1)
            datePicker.show()
        }

        binding.tvItrModeOfTransaction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.tvItrModeOfTransaction.onItemSelectedListener = this
                vitrModeOfTransacValue = adapterView?.getItemAtPosition(position).toString()
            }
        }

        binding.tvItrTypeOfConsultation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.tvItrTypeOfConsultation.onItemSelectedListener = this
                vitrTypeOfConsult = adapterView?.getItemAtPosition(position).toString()
            }
        }

        binding.tvItrSpeDiagnosis.setOnClickListener(View.OnClickListener {

            dialog = Dialog(this@PatientsDetailsActivityITR)
            dialog!!.setContentView(R.layout.itr_dialog_diagnosis)
            dialog!!.window!!.setLayout(750, 1000)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.show()

            val tmp = binding.tvItrSpeDiagnosis.text.toString()

            val etSearch = dialog!!.findViewById<EditText>(R.id.etSearch)
            val listView = dialog!!.findViewById<ListView>(R.id.listView)

            val adapterA = ArrayAdapter(this@PatientsDetailsActivityITR, android.R.layout.simple_list_item_1, spinnerValueA)
            listView.adapter = adapterA

            etSearch.setText(tmp)

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int,
                ) {
                }
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    adapterA.filter.filter(s)
                }
                override fun afterTextChanged(s: Editable) {}
            })

            listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->

                    if(adapterA.getItem(position).toString() == "Others"){

                        binding.temptvSpecificDiagnosis.visibility = View.VISIBLE
                        binding.temptvSpecificDiagnosis.setText("")
                    }else{

                        binding.temptvSpecificDiagnosis.visibility = View.GONE
                    }

                    binding.tvItrSpeDiagnosis.text = adapterA.getItem(position)
                    dialog!!.dismiss()
                }
        })

        binding.btnUpdate.setOnClickListener {

            val fields = HashMap<String, Any>()
            fields.clear()

            fields["vitrModeOfTransac"] = vitrModeOfTransacValue
            fields["vitrDateOfConsult"] = binding.tvItrDate.text.toString()
            fields["vitrConsultTime"] = binding.tvItrTime.text.toString()
            fields["vitrBP"] = binding.tvItrBP.text.toString()
            fields["vitrTemp"] = binding.tvItrTemp.text.toString()
            fields["vitrHeight"] = binding.tvItrHeight.text.toString()
            fields["vitrWeight"] = binding.tvItrWeight.text.toString()
            fields["vitrTypeOfConsult"] = vitrTypeOfConsult
            fields["vitrComplaint"] = binding.tvItrComplaint.text.toString()
            fields["vitrDiagnosis"] = binding.tvItrComplaint.text.toString()
            fields["vitrSpecificDiagnosis"] = binding.tvItrSpeDiagnosis.text.toString()
            fields["vitrTreatment"] = binding.tvItrTreatment.text.toString()
            fields["vitrDateOfReturn"] = binding.tvItrReturnDate.text.toString()

            fields["userKey2"] = Global.userKey.toString()
            fields["zupDateTime"] = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())

            FirebaseDatabase.getInstance().getReference("MainConsultations/$mainPatientKey/$mainConsultationsKey").updateChildren(fields)


            val fields1 = java.util.HashMap<String, Any>()
            fields1.clear()
            fields1["MainConsultationsKey"] = mainConsultationsKey
            fields1["MainPatientKey"] = mainPatientKey
            fields1["vitrDateOfConsult"] = binding.tvItrDate.text.toString()
            fields1["vitrConsultTime"] = binding.tvItrTime.text.toString()
            fields1["vitrSpecificDiagnosis"] = binding.tvItrSpeDiagnosis.text.toString()

            FirebaseDatabase.getInstance().getReference("ReportConsultations/$mainConsultationsKey").updateChildren(fields1)

            Toast.makeText(this, "Update Complete.", Toast.LENGTH_LONG).show()

            val intent = Intent(this, DetailsITR::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun diagnosisList2() {

        spinnerValueA!!.clear()

        val dbRef = FirebaseDatabase.getInstance().reference.child("SpecificDiagnosis").orderByChild("vitrSpecificDiagnosis")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (patSnap in snapshot.children) {
                        val usersData = patSnap.getValue(SpecificDiagnosis::class.java)
                        val vitrSpecificDiagnosis = usersData?.vitrSpecificDiagnosis
                        spinnerValueA!!.add(vitrSpecificDiagnosis.toString())
                    }

                    spinnerValueA!!.add("Others")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showError() {
        val view: View = LayoutInflater.from(this@PatientsDetailsActivityITR)
            .inflate(R.layout.layout_dialog_error, null)
        val builder = AlertDialog.Builder(this@PatientsDetailsActivityITR, R.style.AlertDialogTheme).setView(view)
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
}