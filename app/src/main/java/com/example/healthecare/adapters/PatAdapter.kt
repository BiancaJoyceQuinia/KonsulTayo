package com.example.healthecare.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthecare.R
import com.example.healthecare.models.PatientsModel
import com.google.firebase.database.FirebaseDatabase

class PatAdapter (private val patList: ArrayList<PatientsModel>) :
    RecyclerView.Adapter<PatAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itr_pat_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    @SuppressLint("SetTextI18n")

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentPat = patList[position]

        holder.vitrSpecificDiagnosis.text   = currentPat.vitrSpecificDiagnosis
        holder.vitrDateOfConsult.text       = currentPat.vitrDateOfConsult + " : " + currentPat.vitrConsultTime
        holder.vitrModeTransaction.text     = currentPat.vitrModeOfTransac

    }

    override fun getItemCount(): Int {
        return patList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {


        val vitrSpecificDiagnosis : TextView = itemView.findViewById(R.id.vitrSpecificDiagnosis)
        val vitrDateOfConsult : TextView = itemView.findViewById(R.id.vitrDateOfConsult)
        val vitrModeTransaction : TextView = itemView.findViewById(R.id.vitrModeTransaction)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

}