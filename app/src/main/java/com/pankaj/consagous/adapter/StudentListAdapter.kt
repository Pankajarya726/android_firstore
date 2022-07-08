package com.pankaj.consagous.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.pankaj.consagous.R
import com.pankaj.consagous.StudentItemClickListener
import com.pankaj.consagous.data_class.Student

class StudentListAdapter(val studentList:ArrayList<Student>, val context: Context,val  listener: StudentItemClickListener) : RecyclerView.Adapter<StudentListAdapter
.ViewHolder>() {



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName :TextView= itemView.findViewById<TextView>(R.id.tv_name)
        val cardView :CardView= itemView.findViewById<CardView>(R.id.cv_student)
        val tvRollNo :TextView= itemView.findViewById<TextView>(R.id.tv_roll_no)
        val tvEmail :TextView= itemView.findViewById<TextView>(R.id.tv_email)
        val tvClass :TextView= itemView.findViewById<TextView>(R.id.tv_class)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = studentList[position]
        holder.tvName.text = "Name : ${student.name}"
        holder.tvRollNo.text = "Roll no. : ${student.rollNo}"
        holder.tvEmail.text = "Email : ${student.email}"
        holder.tvClass.text = "Class : ${student.classs}"
        holder.cardView.setOnClickListener(View.OnClickListener {

          listener.onItemClick(student)



        })

    }

    override fun getItemCount(): Int {
        return  studentList.size
    }
}