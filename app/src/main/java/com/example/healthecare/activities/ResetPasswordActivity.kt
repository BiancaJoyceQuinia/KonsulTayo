package com.example.healthecare.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthecare.R
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var etEnterEmail: EditText
    private lateinit var btnReset: Button

    private lateinit var tvLoginAgain: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        etEnterEmail = findViewById(R.id.etEnterEmail)
        btnReset = findViewById(R.id.btnReset)

        auth = FirebaseAuth.getInstance()

        val tvLoginAgain = findViewById<TextView>(R.id.tvLoginAgain)
        tvLoginAgain.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnReset.setOnClickListener {
            val validEmail = etEnterEmail.text.toString()
            if(validEmail.isEmpty()){
                etEnterEmail.error = "Please enter your email"
            }
            else {
                auth.sendPasswordResetEmail(validEmail)
                    .addOnSuccessListener {
                        Toast.makeText(this,
                            "Reset Password Link Successfully Send",
                            Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,
                            "Email does not exist",
                            Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}