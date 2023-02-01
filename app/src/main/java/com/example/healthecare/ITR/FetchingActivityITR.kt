package com.example.healthecare.ITR

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.healthecare.R
import com.example.healthecare.adapters.PatAdapter
import com.example.healthecare.global.Global
import com.example.healthecare.models.PatientsModel
import com.google.firebase.database.*
import java.util.*

class FetchingActivityITR: AppCompatActivity() {

    private lateinit var patRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var patList: ArrayList<PatientsModel>
    private lateinit var tempPatList: ArrayList<PatientsModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching_itr)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = "Individual Treatment Record"

        val key =  intent.getStringExtra("key").toString()

        patRecyclerView = findViewById(R.id.rvPatients)
        patRecyclerView.layoutManager = LinearLayoutManager(this)
        patRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        patList = arrayListOf<PatientsModel>()
        tempPatList = arrayListOf<PatientsModel>()

        getPatientsData(key)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_search,menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView

        searchView.queryHint = "Search by Date of Consultation"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                tempPatList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    patList.forEach {
                        if(it.vitrDateOfConsult!!.toLowerCase(Locale.getDefault()).contains(searchText)){
                            tempPatList.add(it)
                        }
                    }
                    patRecyclerView.adapter!!.notifyDataSetChanged()
                }
                else{
                    tempPatList.clear()
                    tempPatList.addAll(patList)
                    patRecyclerView.adapter!!.notifyDataSetChanged()
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun getPatientsData(key: String) {

        patRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val mainPatientKey = key.toString()

        dbRef = FirebaseDatabase.getInstance().getReference("MainConsultations/$key")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                patList.clear()

                if(snapshot.exists()){

                    for(patSnap in snapshot.children){

                        val patData = patSnap.getValue(PatientsModel::class.java)

                        patData?.MainPatientKey = mainPatientKey
                        patData?.MainConsultationsKey = patSnap?.key.toString()

                        patList.add(patData!!)
                    }

//                    patList.sortBy { it.vitrDateOfConsult }
                    patList.sortByDescending { it.vitrDateOfConsult }

                    tempPatList.clear()
                    tempPatList.addAll(patList)

                    val mAdapter = PatAdapter(tempPatList)

                    patRecyclerView.adapter = mAdapter

                    patRecyclerView.adapter!!.notifyDataSetChanged()

                    mAdapter.setOnItemClickListener(object : PatAdapter.onItemClickListener{

                        override fun onItemClick(position: Int) {

                            Global.mainPatientKey = tempPatList[position].MainPatientKey.toString()
                            Global.mainConsultationsKey = tempPatList[position].MainConsultationsKey.toString()

                            val intent = Intent(this@FetchingActivityITR, DetailsITR::class.java)

                            intent.putExtra("MainPatientKey", tempPatList[position].MainPatientKey)
                            intent.putExtra("MainConsultationsKey", tempPatList[position].MainConsultationsKey)
                            intent.putExtra("vitrLName", tempPatList[position].vitrLName)
                            intent.putExtra("vitrFName", tempPatList[position].vitrFName)
                            intent.putExtra("vitrMName", tempPatList[position].vitrMName)
                            intent.putExtra("vitrSuffix", tempPatList[position].vitrSuffix)
                            intent.putExtra("vitrFullName", tempPatList[position].vitrFullName)
                            intent.putExtra("vitrResAdd", tempPatList[position].vitrResAdd)
                            intent.putExtra("vitrModeOfTransac", tempPatList[position].vitrModeOfTransac)
                            intent.putExtra("vitrDateOfConsult", tempPatList[position].vitrDateOfConsult)
                            intent.putExtra("vitrConsultTime", tempPatList[position].vitrConsultTime)
                            intent.putExtra("vitrBP", tempPatList[position].vitrBP)
                            intent.putExtra("vitrTemp", tempPatList[position].vitrTemp)
                            intent.putExtra("vitrHeight", tempPatList[position].vitrHeight)
                            intent.putExtra("vitrWeight", tempPatList[position].vitrWeight)
                            intent.putExtra("vitrTypeOfConsult", tempPatList[position].vitrTypeOfConsult)
                            intent.putExtra("vitrComplaint", tempPatList[position].vitrComplaint)
                            intent.putExtra("vitrDiagnosis", tempPatList[position].vitrDiagnosis)
                            intent.putExtra("vitrSpecificDiagnosis", tempPatList[position].vitrSpecificDiagnosis)
                            intent.putExtra("vitrTreatment", tempPatList[position].vitrTreatment)
                            intent.putExtra("vitrTreatment", tempPatList[position].vitrTreatment)
                            intent.putExtra("vitrDateOfReturn", tempPatList[position].vitrDateOfReturn)
                            startActivity(intent)
                        }
                    })

                    patRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}