package com.pankaj.consagous.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pankaj.consagous.R
import com.pankaj.consagous.data_class.Student
import com.pankaj.consagous.data_class.Test

class TestListAdapter(val testList:ArrayList<Test>, val context: Context) : RecyclerView.Adapter<TestListAdapter.ViewHolder>() {



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTestNo :TextView= itemView.findViewById<TextView>(R.id.tv_test_no)
        val tvDate :TextView= itemView.findViewById<TextView>(R.id.tv_date)
        val tvSubject :TextView= itemView.findViewById<TextView>(R.id.tv_subject)
        val tvTopic :TextView= itemView.findViewById<TextView>(R.id.tv_topic)
        val tvMarks :TextView= itemView.findViewById<TextView>(R.id.tv_marks)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val test = testList[position]
        holder.tvTestNo.text = "Test - ${test.testNo}"
        holder.tvDate.text = "${test.date}"
        holder.tvSubject.text = "Subject - ${test.subject} "
        holder.tvTopic.text = "(${test.topic})"
        holder.tvMarks.text = test.maxMark.toString()

    }

    override fun getItemCount(): Int {
        return  testList.size
    }
}