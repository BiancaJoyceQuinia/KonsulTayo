package com.example.healthecare.PER

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthecare.global.Global
import com.example.healthecare.R
import com.example.healthecare.adapters.PerAdapter
import com.example.healthecare.models.PERModel
import com.google.firebase.database.*
import java.util.*

import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class FetchingActivityPER: AppCompatActivity() {

    private lateinit var perRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var perList: ArrayList<PERModel>
    private lateinit var tempPerList: ArrayList<PERModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var btnPER: LinearLayout

    private lateinit var inflater: LayoutInflater
    private lateinit var view: View

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching_per)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Patient Enrollment Record"

//        supportActionBar!!.setLogo(R.drawable.logo_name)
//        supportActionBar!!.setDisplayUseLogoEnabled(true)

//        inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        view = inflater.inflate(R.layout.image_holder, null)
//        supportActionBar!!.customView = view

//        supportActionBar!!.setLogo(R.drawable.logo_name)
//        supportActionBar!!.setDisplayUseLogoEnabled(true)

//        supportActionBar!!.setDisplayShowHomeEnabled(true)
//        supportActionBar!!.setIcon(R.layout.image_holder)

        perRecyclerView = findViewById(R.id.rvPatients)
        perRecyclerView.layoutManager = LinearLayoutManager(this)
        perRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)
        btnPER = findViewById(R.id.btnPER)

        perList = arrayListOf<PERModel>()
        tempPerList = arrayListOf<PERModel>()
        getPERData()

        btnPER.setOnClickListener {
            val intent = Intent(this, PatientEnrollmentRecord::class.java)
            startActivity(intent)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_search,menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView

        searchView.queryHint = "Search by Full Name"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                tempPerList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    perList.forEach {
                        if(it.vperFullName!!.lowercase(Locale.getDefault()).contains(searchText)){
                            tempPerList.add(it)
                        }
                    }
                    perRecyclerView.adapter!!.notifyDataSetChanged()
                }
                else{
                    tempPerList.clear()
                    tempPerList.addAll(perList)
                    perRecyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun getPERData() {

        perRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("MainPatient")

        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                perList.clear()

                if(snapshot.exists()){

                    for(perSnap in snapshot.children){

                        val perData = perSnap.getValue(PERModel::class.java)

                        perData?.key = perSnap?.key.toString()

                        perList.add(perData!!)
                    }

                    perList.sortBy { it.vperFullName }

                    tempPerList.addAll(perList)

                    val mAdapter = PerAdapter(tempPerList)

                    perRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : PerAdapter.onItemClickListener{

                        override fun onItemClick(position: Int) {

                            Global.key = tempPerList[position].key.toString()

                            val intent = Intent(this@FetchingActivityPER, DetailsPER::class.java)

                            intent.putExtra("key", tempPerList[position].key)
                            intent.putExtra("vperLName", tempPerList[position].vperLName)
                            intent.putExtra("vperFName", tempPerList[position].vperFName)
                            intent.putExtra("vperMName", tempPerList[position].vperMName)
                            intent.putExtra("vperSuffix", tempPerList[position].vperSuffix)
                            intent.putExtra("vperFullName", tempPerList[position].vperFullName)
                            intent.putExtra("vperAge", tempPerList[position].vperAge)
                            intent.putExtra("vperSex", tempPerList[position].vperSex)
                            intent.putExtra("vperBirthdate", tempPerList[position].vperBirthdate)
                            intent.putExtra("vperBirthplace", tempPerList[position].vperBirthplace)
                            intent.putExtra("vperCivilStatus", tempPerList[position].vperCivilStatus)
                            intent.putExtra("vperSpouse", tempPerList[position].vperSpouse)
                            intent.putExtra("vperEducAttain", tempPerList[position].vperEducAttain)
                            intent.putExtra("vperEmpStatus", tempPerList[position].vperEmpStatus)
                            intent.putExtra("vperFamMem", tempPerList[position].vperFamMem)
                            intent.putExtra("vperMomName", tempPerList[position].vperMomName)
                            intent.putExtra("vperMomBirth", tempPerList[position].vperMomBirth)
                            intent.putExtra("vperBrgy", tempPerList[position].vperBrgy)
                            intent.putExtra("vperResAdd", tempPerList[position].vperResAdd)
                            intent.putExtra("vperDadName", tempPerList[position].vperDadName)
                            intent.putExtra("vperDadBirth", tempPerList[position].vperDadBirth)
                            intent.putExtra("vperContactNum", tempPerList[position].vperContactNum)
                            intent.putExtra("vper4psMem", tempPerList[position].vper4psMem)
                            intent.putExtra("vperPhilHealthMem", tempPerList[position].vperPhilHealthMem)
                            intent.putExtra("vperStatusType", tempPerList[position].vperStatusType)
                            intent.putExtra("vperPhilHealthNo", tempPerList[position].vperPhilHealthNo)

                            //intent.putExtra("pos", tempPerList[position].pos)

                            startActivity(intent)
                        }
                    })
                    perRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}