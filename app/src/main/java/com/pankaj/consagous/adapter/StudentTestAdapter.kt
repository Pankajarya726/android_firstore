package com.pankaj.consagous.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pankaj.consagous.R
import com.pankaj.consagous.data_class.StudentTest

class StudentTestAdapter(val testList: ArrayList<StudentTest>, val context: Context) :
    RecyclerView.Adapter<StudentTestAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTestNo: TextView = itemView.findViewById<TextView>(R.id.tv_test_no)
        val tvDate: TextView = itemView.findViewById<TextView>(R.id.tv_date)
        val tvSubject: TextView  = itemView.findViewById<TextView>(R.id.tv_subject)
        val tvTopic: TextView =  itemView.findViewById<TextView>(R.id.tv_topic)
        val tvTotalMarks: TextView =  itemView.findViewById<TextView>(R.id.tv_total_mark)
        val tvMarkObtained: TextView =  itemView.findViewById<TextView>(R.id.tv_mark_obtained)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentTestAdapter.ViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.student_test_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val test = testList[position]
        holder.tvDate.text = "Date : "+test.date
        holder.tvTestNo.text ="Test No : "+ test.testNo.toString()
        holder.tvSubject.text = "Subject : "+test.subject
        holder.tvTotalMarks.text = "Maximum mark : "+test.maxMark.toString()
        holder.tvTopic.text = "Topic : "+test.topic
        holder.tvMarkObtained.text = "Mark Obtained : "+test.markObtained.toString() +"%"
    }

    override fun getItemCount(): Int {
        return  testList.size
    }
}