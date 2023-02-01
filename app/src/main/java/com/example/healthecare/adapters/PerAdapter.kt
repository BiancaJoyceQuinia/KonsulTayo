package com.example.healthecare.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthecare.R
import com.example.healthecare.models.PERModel
import com.google.firebase.database.FirebaseDatabase

class PerAdapter (private val perList: ArrayList<PERModel>) :
        RecyclerView.Adapter<PerAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener


    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.per_pat_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPat = perList[position]

        holder.tvPatName.text = currentPat.vperFullName
        holder.vperBrgy.text = currentPat.vperBrgy
        holder.vperContactNum.text = currentPat.vperContactNum
    }

    override fun getItemCount(): Int {
        return perList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvPatName : TextView = itemView.findViewById(R.id.tvPatName)
        val vperBrgy : TextView = itemView.findViewById(R.id.vperBrgy)
        val vperContactNum : TextView = itemView.findViewById(R.id.vperContactNum)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
        }