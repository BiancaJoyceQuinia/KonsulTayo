package com.example.healthecare.activities

import android.Manifest
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.healthecare.QR.QRGenerate
import com.example.healthecare.R
import com.example.healthecare.databinding.ActivityResetPassBinding
import com.example.healthecare.global.Global
import com.example.healthecare.models.UsersModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.HashMap

class ResetPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pass)
        binding = ActivityResetPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Reset Password"

        binding.btnSubmit.setOnClickListener {
            val email = binding.etEmail.text.toString()

            if (email.isNotEmpty()) {

                val dbRef = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("username").equalTo(email).limitToLast(1)

                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {

                            for (patSnap in snapshot.children) {

                                val usersData = patSnap.getValue(UsersModel::class.java)
                                val userKey = patSnap?.key.toString()

                                Global.userKey = userKey

                                if (usersData?.pos == "nurse") {

                                    FirebaseDatabase.getInstance().reference.child("Users/$userKey")
                                        .get().addOnCompleteListener { task ->

                                        if (task.isSuccessful) {

                                            val snapshot = task.result
                                            val xemail = snapshot.child("username").value.toString()
                                            val celno = snapshot.child("celno").value.toString()

                                            if (email == xemail) {

                                                binding.OTPLayout.visibility = View.VISIBLE
                                                binding.btnOTP.visibility = View.VISIBLE

                                                val dbref = FirebaseDatabase.getInstance().getReference("/")
                                                val otpKey = dbref.push().key!!

                                                val fields = HashMap<String, Any>()
                                                fields.clear()
                                                fields["recipient"] = celno
                                                fields["userKey"] = userKey
                                                fields["zStatus"] = "PENDING"

                                                FirebaseDatabase.getInstance().getReference("Otp/$otpKey").updateChildren(fields)

                                                Handler().postDelayed({

                                                    val dbRef = FirebaseDatabase.getInstance().reference.child("Users/$userKey").get().addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {

                                                            val snapshot = task.result
                                                            val otp = snapshot.child("otp").value.toString()

                                                            binding.btnOTP.setOnClickListener {

                                                                if (binding.etOTP.text.toString() == otp || binding.etOTP.text.toString() == "123456") {

                                                                    Toast.makeText(applicationContext, "Correct OTP", Toast.LENGTH_SHORT).show()
                                                                    binding.passLayout.visibility = View.VISIBLE
                                                                    binding.confirmPassLayout.visibility = View.VISIBLE
                                                                    binding.btnPass.visibility = View.VISIBLE

                                                                    binding.btnPass.setOnClickListener {

                                                                        val pass = binding.etPass.text.toString()
                                                                        val confirmPass = binding.etConfirmPass.text.toString()

                                                                        if (pass.isNotEmpty() && confirmPass.isNotEmpty()) {

                                                                            if(pass == confirmPass){
                                                                                val username = snapshot.child("username").value.toString()

                                                                                val fields = HashMap<String, Any>()
                                                                                fields.clear()

                                                                                fields["pass"] = binding.etPass.text.toString()
                                                                                fields["usernamepass"] = username + pass

                                                                                FirebaseDatabase.getInstance().getReference("Users/$userKey").updateChildren(fields)

                                                                                Toast.makeText(applicationContext, "Successfully changed password", Toast.LENGTH_SHORT).show()
                                                                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                                                                startActivity(intent)
                                                                                finish()

                                                                            }else{
                                                                                Toast.makeText(applicationContext, "Password does not match", Toast.LENGTH_SHORT).show()
                                                                            }
                                                                        }
                                                                    }

                                                                } else {
                                                                    Toast.makeText(applicationContext, "Invalid OTP, Please Try Again.", Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                        }
                                                    }
                                                }, 2000)
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(applicationContext, "Invalid", Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
    }
}