package com.admins.dq.diagnosis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.admins.dq.R
import com.admins.dq.model.ResultsModel

class ResultsAdapter(
    val data: List<ResultsModel>,
    val mContext: Context
) :
    RecyclerView.Adapter<ResultsAdapter.MyVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        return MyVH(
            LayoutInflater.from(mContext).inflate(R.layout.results_recycler_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {

        holder.bindTo(data[position])

    }

    inner class MyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_horizontal)
        val text: TextView = itemView.findViewById(R.id.text_item)
        val freq: TextView = itemView.findViewById(R.id.text_freq)
        fun bindTo(
            result: ResultsModel
        ) {
            progressBar.max = 100
            progressBar.progress = result.fRRQ?.toInt()!!
            text.text = result.illnessName!!
            freq.text = String.format("%.2f", result.fRRQ) + "%"
        }


    }
}

