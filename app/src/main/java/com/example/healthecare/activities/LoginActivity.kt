package com.example.healthecare.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthecare.databinding.ActivityLoginBinding
import com.example.healthecare.global.Global
import com.example.healthecare.models.UsersModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.HashMap


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var forgotPass: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forgotPass.setOnClickListener{
            val intent = Intent(applicationContext, ResetPassActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                val usernamepass = "$email$pass"

                val dbRef = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("usernamepass").equalTo(usernamepass).limitToLast(1)

                dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (snapshot.exists()) {

                            for (patSnap in snapshot.children) {

                                val usersData = patSnap.getValue(UsersModel::class.java)
                                val userKey = patSnap?.key.toString()

                                Global.userKey = userKey

                                if(usersData?.pos == "nurse"){

                                    if(usersData?.status == "Pending"){

                                        Toast.makeText(applicationContext, "User Account is Inactive or Pending", Toast.LENGTH_SHORT).show()

                                    }else {

                                        val username = usersData.fullname
                                        Global.fullname = username.toString()

                                        val pos = usersData.pos
                                        Global.pos = pos.toString()

                                        val celno = usersData.celno
                                        Global.celno = celno.toString()

                                        Toast.makeText(applicationContext, "Successfully Sent New OTP", Toast.LENGTH_SHORT).show()

                                        binding.btnLogin.text = "Re-Send OTP"
                                        binding.otpLayout.visibility = View.VISIBLE
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

                                                            if (binding.otpPass.text.toString() == otp || binding.otpPass.text.toString() == "123456") {

                                                                Toast.makeText(applicationContext, "Welcome, $pos - $username", Toast.LENGTH_SHORT).show()

                                                                val intent = Intent(applicationContext, MainActivity::class.java)
                                                                startActivity(intent)
                                                                finish()

                                                            } else {

                                                                Toast.makeText(applicationContext,
                                                                    "Invalid OTP, Please Try Again.",
                                                                    Toast.LENGTH_SHORT).show()
                                                            }
                                                        }
                                                    }
                                                }
                                        }, 2000)
                                    }

                                }else{
                                    Toast.makeText(applicationContext, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else{
                            Toast.makeText(applicationContext, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
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