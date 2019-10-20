package com.admins.dq.drawerui.home.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.admins.dq.R
import com.admins.dq.sessionStart.SessionActivity
import com.admins.dq.model.oldModel.Patient
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PatientsRecyclerAdapter(
    private val mContext: Context,
    var data: ArrayList<Patient>
) : DynamicSearchAdapter<Patient>(data) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        return MyVH(
            LayoutInflater.from(mContext).inflate(R.layout.patients_recycler_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        holder.bindTo(data[position])
    }

    inner class MyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text = itemView.findViewById<TextView>(R.id.item_name)
        val card = itemView.findViewById<CardView>(R.id.item_card)
        fun bindTo(patient: Patient) {
            text.text = patient.name
            card.setOnClickListener {
                //                mContext.startActivity(Intent())
                val builder = MaterialAlertDialogBuilder(mContext)
                    .setCancelable(true)
                    .setTitle("Session Starting")
                    .setIcon(R.drawable.ic_login)
                    .setMessage("Start a Session for ${patient.name}")
                    .setPositiveButton("Start") { dialogInterface, i ->
                        mContext.startActivity(
                            Intent(mContext, SessionActivity::class.java)
                        )
                    }
                    .setNegativeButton("Cancel") { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }
                val dialog = builder.create()
                dialog.show()
            }
        }

    }
}