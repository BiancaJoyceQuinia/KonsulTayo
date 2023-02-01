package com.example.healthecare.PER

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.healthecare.global.Global
import com.example.healthecare.R
import com.example.healthecare.databinding.ActivityPatientDetailsPerBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class PatientsDetailsActivityPER: AppCompatActivity() {

    private lateinit var key: String
    private lateinit var binding: ActivityPatientDetailsPerBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var vperSex = ""
        var vperCivilStatus = ""
        var vperEducAttain = ""
        var vperEmpStatus = ""
        var vperFamMem = ""
        var vperBrgy = ""

        val date = Calendar.getInstance()
        var year = date.get(Calendar.YEAR)
        var month = date.get(Calendar.MONTH)
        var day = date.get(Calendar.DAY_OF_MONTH)

        val date1 = Calendar.getInstance()
        var year1 = date1.get(Calendar.YEAR)
        var month1 = date1.get(Calendar.MONTH)
        var day1 = date1.get(Calendar.DAY_OF_MONTH)

        val date2 = Calendar.getInstance()
        var year2 = date2.get(Calendar.YEAR)
        var month2 = date2.get(Calendar.MONTH)
        var day2 = date2.get(Calendar.DAY_OF_MONTH)

        binding = ActivityPatientDetailsPerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Patient Enrollment Record Update"

        key = Global.key

        val dbRef = FirebaseDatabase.getInstance().reference.child("MainPatient/$key").get().addOnCompleteListener { task ->

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
                val pos = snapshot.child("pos").value.toString()

                Global.vperBirthdate = vperBirthdate
                Global.vperMomBirth = vperMomBirth
                Global.vperDadBirth = vperDadBirth
                //Global.vperPhilHealthMem = vperPhilHealthMem

                binding.tvPerLName.setText(vperLName)
                binding.tvPerFName.setText(vperFName)
                binding.tvPerMName.setText(vperMName)
                binding.tvPerSuffix.setText(vperSuffix)
                binding.tvPerFullName.setText(vperFullName)
                binding.tvPerAge.setText(vperAge)
                binding.tvPerSex.setSelection(resources.getStringArray(R.array.gender).indexOf(vperSex))
                binding.tvPerBirthdate.setText(vperBirthdate)
                binding.tvPerBirthplace.setText(vperBirthplace)
                binding.tvPerCivilStatus.setSelection(resources.getStringArray(R.array.civilStatus).indexOf(vperCivilStatus))
                binding.tvPerSpouse.setText(vperSpouse)
                binding.tvPerEducAttain.setSelection(resources.getStringArray(R.array.educAttainment).indexOf(vperEducAttain))
                binding.tvPerEmpStats.setSelection(resources.getStringArray(R.array.empStatus).indexOf(vperEmpStatus))
                binding.tvPerFamMem.setSelection(resources.getStringArray(R.array.famMember).indexOf(vperFamMem))
                binding.tvPerMomName.setText(vperMomName)
                binding.tvPerMomBirth.setText(vperMomBirth)
                binding.tvPerResAdd.setText(vperResAdd)
                binding.tvPerBrgy.setSelection(resources.getStringArray(R.array.barangay).indexOf(vperBrgy))
                binding.tvPerDadName.setText(vperDadName)
                binding.tvPerDadBirth.setText(vperDadBirth)
                binding.tvPerContactNum.setText(vperContactNum)
                binding.tvPer4psMem.setText(vper4psMem)
                binding.tvPerPhilMem.setText(vperPhilHealthMem)
                binding.tvPerStatusType.setText(vperStatusType)
                binding.tvPerPhilHealthNo.setText(vperPhilHealthNo)
                binding.tvPos.setText(pos)


                if(vper4psMem.toString().uppercase() == "YES"){
                    binding.rb4ps1.isChecked = true
                    binding.rb4ps2.isChecked = false
                }else{
                    binding.rb4ps1.isChecked = false
                    binding.rb4ps2.isChecked = true
                }

                if(vperPhilHealthMem.toString().uppercase() == "YES"){
                    binding.rbPhilHealthMem1.isChecked = true
                    binding.rbPhilHealthMem2.isChecked = false

                    binding.rgStatusType.isVisible

                    if(vperStatusType.toString().uppercase() == "DEPENDENT"){
                        binding.rbStatus1.isChecked = true
                        binding.rbStatus2.isChecked = false
                    }else{
                        binding.rbStatus1.isChecked = false
                        binding.rbStatus2.isChecked = true
                    }

                } else {
                    binding.rgStatusType.clearCheck()
                    binding.tvPerStatusType.setText("")

                    binding.rbPhilHealthMem1.isChecked = false
                    binding.rbPhilHealthMem2.isChecked = true

                    binding.rgStatusType.isInvisible

                    binding.tvPerStatusType.setText("")
                }
            } else {
                showError()
            }

        }.addOnFailureListener {
            showError()
        }

        binding.rg4psMem.setOnCheckedChangeListener { rg4psMem, checkedId ->
            val rb = findViewById<RadioButton>(checkedId)
            if(rb!=null)
                binding.tvPer4psMem.setText(rb.text.toString())
        }

        binding.rgPhilHealthMem.setOnCheckedChangeListener { rgPhilHealthMem, checkedId ->

            binding.llStatus.isVisible = R.id.rbPhilHealthMem1 == checkedId
            binding.llPhilHealthNo.isInvisible = R.id.rbPhilHealthMem2 == checkedId

            val rb = findViewById<RadioButton>(checkedId)
            if(rb!=null)
                binding.tvPerPhilMem.setText(rb.text.toString())

                    if(rb.text.toString().uppercase() == "YES"){
                        binding.llPhilHealthNo.isInvisible = true
                        binding.rbStatus1.isChecked = true


                        //binding.tvPerPhilHealthNo.setText("")

                    }else{
                        binding.tvPerStatusType.setText("")
                        binding.tvPerPhilHealthNo.setText("")
                    }
        }

        binding.rgStatusType.setOnCheckedChangeListener { rgStatusType, checkedId ->

            binding.llPhilHealthNo.isInvisible = R.id.rbStatus1 == checkedId
            binding.llPhilHealthNo.isVisible = R.id.rbStatus2 == checkedId

            val rb = findViewById<RadioButton>(checkedId)
            if(rb!=null)
                binding.tvPerStatusType.setText(rb.text.toString())
        }



        binding.btnDate.setOnClickListener {

            if(Global.vperBirthdate != "") {
                val temp = Global.vperBirthdate.split("-")
                if (temp.isNotEmpty()) {
                    year = temp[0].toInt()
                    month = temp[1].toInt() - 1
                    day = temp[2].toInt()
                }
            }

            var datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker1, mYear, mMonth, mDay ->
            binding.tvPerBirthdate.setText("" + mYear + "-" + (mMonth + 1) + "-" + mDay)
            }, year, month, day)
            datePicker.show()
        }

        binding.btnDate1.setOnClickListener {

            if(Global.vperMomBirth != ""){

                val temp1 = Global.vperMomBirth.split("-")

                if(temp1.isNotEmpty()){
                    year1 = temp1[0].toInt()
                    month1 = temp1[1].toInt() - 1
                    day1 = temp1[2].toInt()
                }
            }

            var datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker1, mYear, mMonth, mDay ->
                binding.tvPerMomBirth.setText("" + mYear + "-" + (mMonth + 1) + "-" + mDay)
            }, year1, month1, day1)
            datePicker.show()
        }

        binding.btnDate2.setOnClickListener {

            if(Global.vperDadBirth != "") {
                val temp2 = Global.vperDadBirth.split("-")
                if (temp2.isNotEmpty()) {
                    year2 = temp2[0].toInt()
                    month2 = temp2[1].toInt() - 1
                    day2 = temp2[2].toInt()
                }
            }

            var datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker1, mYear, mMonth, mDay ->
                binding.tvPerDadBirth.setText("" + mYear + "-" + (mMonth + 1) + "-" + mDay)
            }, year2, month2, day2)
            datePicker.show()
        }

        binding.tvPerSex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.tvPerSex.onItemSelectedListener = this
                vperSex = adapterView?.getItemAtPosition(position).toString()
            }
        }

        binding.tvPerCivilStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.tvPerCivilStatus.onItemSelectedListener = this
                vperCivilStatus = adapterView?.getItemAtPosition(position).toString()
            }
        }

        binding.tvPerEducAttain.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.tvPerEducAttain.onItemSelectedListener = this
                vperEducAttain = adapterView?.getItemAtPosition(position).toString()
            }
        }

        binding.tvPerEmpStats.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.tvPerEmpStats.onItemSelectedListener = this
                vperEmpStatus = adapterView?.getItemAtPosition(position).toString()
            }
        }

        binding.tvPerFamMem.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.tvPerFamMem.onItemSelectedListener = this
                vperFamMem = adapterView?.getItemAtPosition(position).toString()
            }
        }

        binding.tvPerBrgy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.tvPerBrgy.onItemSelectedListener = this
                vperBrgy = adapterView?.getItemAtPosition(position).toString()
            }
        }

        binding.btnUpdate.setOnClickListener {

            val fields = HashMap<String, Any>()
            fields.clear()

            fields["vperLName"] = binding.tvPerLName.text.toString()
            fields["vperFName"] = binding.tvPerFName.text.toString()
            fields["vperMName"] = binding.tvPerMName.text.toString()
            fields["vperSuffix"] = binding.tvPerSuffix.text.toString()
            fields["vperFullName"] = binding.tvPerFullName.text.toString()
            fields["vperAge"] = binding.tvPerAge.text.toString()
            fields["vperSex"] = vperSex
            fields["vperBirthdate"] = binding.tvPerBirthdate.text.toString()
            fields["vperBirthplace"] = binding.tvPerBirthplace.text.toString()
            fields["vperCivilStatus"] = vperCivilStatus
            fields["vperSpouse"] = binding.tvPerSpouse.text.toString()
            fields["vperEducAttain"] = vperEducAttain
            fields["vperEmpStatus"] = vperEmpStatus
            fields["vperFamMem"] = vperFamMem
            fields["vperMomName"] = binding.tvPerMomName.text.toString()
            fields["vperMomBirth"] = binding.tvPerMomBirth.text.toString()
            fields["vperResAdd"] = binding.tvPerResAdd.text.toString()
            fields["vperBrgy"] = vperBrgy
            fields["vperDadName"] = binding.tvPerDadName.text.toString()
            fields["vperDadBirth"] = binding.tvPerDadBirth.text.toString()
            fields["vperContactNum"] = binding.tvPerContactNum.text.toString()
            fields["vper4psMem"] = binding.tvPer4psMem.text.toString()
            fields["vperPhilHealthMem"] = binding.tvPerPhilMem.text.toString()
            fields["vperStatusType"] = binding.tvPerStatusType.text.toString()
            fields["vperPhilHealthNo"] = binding.tvPerPhilHealthNo.text.toString()

            fields["userKey2"] = Global.userKey.toString()
            fields["zupDateTime"] = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())


            FirebaseDatabase.getInstance().getReference("MainPatient/$key").updateChildren(fields)
            Toast.makeText(this, "Updated Successfully", Toast.LENGTH_LONG).show()

            val intent = Intent(this, DetailsPER::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun showError() {
        val view: View = LayoutInflater.from(this@PatientsDetailsActivityPER)
            .inflate(R.layout.layout_dialog_error, null)
        val builder = AlertDialog.Builder(this@PatientsDetailsActivityPER, R.style.AlertDialogTheme).setView(view)
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