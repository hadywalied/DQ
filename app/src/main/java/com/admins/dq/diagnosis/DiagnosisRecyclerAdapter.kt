package com.admins.dq.diagnosis

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.admins.dq.R
import com.admins.dq.model.oldModel.Questions

class DiagnosisRecyclerAdapter(
    val data: List<Questions>,
    val mContext: Context,
    val action: OnButtonClicked
) :
    RecyclerView.Adapter<DiagnosisRecyclerAdapter.MyVH>() {

    interface OnButtonClicked {
        fun onYesClicked(position: Int)

        fun onNoClicked(position: Int)

        fun onCardClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        return MyVH(
            LayoutInflater.from(mContext).inflate(R.layout.question_recycler_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {

        holder.bindTo(data[position], action, position)

    }

    inner class MyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val questionName = itemView.findViewById<TextView>(R.id.question)
        val questionCard = itemView.findViewById<CardView>(R.id.question_card)
        val questionYes = itemView.findViewById<Button>(R.id.answer_yes_item)
        val questionNo = itemView.findViewById<Button>(R.id.answer_no_item)

        fun bindTo(
            questions: Questions,
            listener: OnButtonClicked,
            position: Int
        ) {
            questionName.text = questions.question
            if (TextUtils.equals(questions.answer, "yes")) {
                questionCard.setCardBackgroundColor(Color.GREEN)
            } else {
                questionCard.setCardBackgroundColor(Color.RED)
            }

            questionCard.setOnClickListener { listener.onCardClicked(position) }
            questionYes.setOnClickListener { listener.onYesClicked(position) }
            questionNo.setOnClickListener { listener.onNoClicked(position) }

        }

    }
}

