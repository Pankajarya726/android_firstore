package com.pankaj.consagous.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.pankaj.consagous.R
import com.pankaj.consagous.data_class.Student
import com.pankaj.consagous.data_class.Test
import com.pankaj.consagous.interfaces.TestItemClickListener

class TestListAdapter(val testList:ArrayList<Test>, val context: Context,val listener:TestItemClickListener) : RecyclerView.Adapter<TestListAdapter.ViewHolder>() {



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTestNo :TextView= itemView.findViewById<TextView>(R.id.tv_test_no)
        val tvDate :TextView= itemView.findViewById<TextView>(R.id.tv_date)
        val tvSubject :TextView= itemView.findViewById<TextView>(R.id.tv_subject)
        val tvTopic :TextView= itemView.findViewById<TextView>(R.id.tv_topic)
        val tvMarks :TextView= itemView.findViewById<TextView>(R.id.tv_marks)
        val tvClass :TextView= itemView.findViewById<TextView>(R.id.tv_class)
        val cardView :CardView= itemView.findViewById<CardView>(R.id.card_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val test = testList[position]
        holder.tvTestNo.text = "Test : ${test.testNo}"
        holder.tvDate.text = "Date : ${test.date}"
        holder.tvSubject.text = "Subject : ${test.subject} "
        holder.tvTopic.text = "Topic : "+test.topic
        holder.tvMarks.text = "Max mark : "+test.maxMark.toString()
        holder.tvClass.text = "Class : "+test.classs
        holder.cardView.setOnClickListener(View.OnClickListener {
            listener.onItemClick(test)
        })

    }

    override fun getItemCount(): Int {
        return  testList.size
    }
}