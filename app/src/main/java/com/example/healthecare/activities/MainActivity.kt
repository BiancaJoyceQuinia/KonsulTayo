package com.example.healthecare.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.healthecare.PER.FetchingActivityPER
import com.example.healthecare.QR.QRScanner
import com.example.healthecare.R
import com.example.healthecare.global.Global
import com.example.healthecare.models.UsersModel
import com.example.healthecare.report.ViewReport
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var btnPER: Button
    private lateinit var btnReport: Button
    private lateinit var btnQRScanner: Button

    private lateinit var pos: String
    private lateinit var dbRef: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "KonsulTayo"

//        val pos =  intent.getStringExtra("pos").toString()
//        Global.pos
//        println(pos)

//        val fireBaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
//        fireBaseDatabase.setPersistenceEnabled(true)
//        fireBaseDatabase.setPersistenceCacheSizeBytes(100000000)

        btnPER = findViewById(R.id.btnPER)
        btnReport = findViewById(R.id.btnReport)
        btnQRScanner = findViewById(R.id.btnQRScanner)

        btnPER.setOnClickListener {
            val intent = Intent(this, FetchingActivityPER::class.java)
            startActivity(intent)
        }

        btnReport.setOnClickListener {
            val intent = Intent(this, ViewReport::class.java)
            startActivity(intent)
        }

        btnQRScanner.setOnClickListener {
            val intent = Intent(this, QRScanner::class.java)
            startActivity(intent)
        }

//        val dbRef = FirebaseDatabase.getInstance().reference.child("Users").orderByChild("pos")
//
//        dbRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                if (snapshot.exists()) {
//                    for (patSnap in snapshot.children) {
//
//                        val usersData = patSnap.getValue(UsersModel::class.java)
//
//                        if(usersData?.pos == "nurse"){
//                            val pos = usersData?.pos
//                            println(pos)
//
//                        }else{
//                            Toast.makeText(applicationContext, "Invalid User", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logout -> {
                showLogoutDialog()
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutDialog() {
        val view: View = LayoutInflater.from(this@MainActivity).inflate(R.layout.layout_dialog_logout, null)
        val builder = AlertDialog.Builder(this@MainActivity, R.style.AlertDialogTheme).setView(view)

        builder.setView(view)
        (view.findViewById<View>(R.id.textTitle) as TextView).text =
            resources.getString(R.string.confirm_logout)
        (view.findViewById<View>(R.id.textMessage) as TextView).text =
            resources.getString(R.string.confirmation_logout)

        val alertDialog = builder.create()

        view.findViewById<View>(R.id.btnLogout).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "You have successfully logged out", Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
            finish()
        }

        view.findViewById<View>(R.id.btnCancel).setOnClickListener {
            alertDialog.dismiss()
        }

        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    override fun onBackPressed() {

        if (supportFragmentManager.backStackEntryCount <= 1) {

            AlertDialog.Builder(this).apply {

                setTitle("Please confirm")
                setMessage("Are you sure you want to exit?")

                setPositiveButton("Yes") { _, _ ->
                    // if user press yes, then finish the current activity
                    //super.onBackPressed()

                    finish()
                }

                setNegativeButton("No") { _, _ ->

                    Toast.makeText(
                        this@MainActivity, "RESUME",
                        Toast.LENGTH_LONG
                    ).show()
                }

                setCancelable(true)

            }.create().show()
        }

        if (supportFragmentManager.backStackEntryCount >= 2) {

            super.onBackPressed()

        }
    }
}