package com.pankaj.consagous.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pankaj.consagous.R
import com.pankaj.consagous.data_class.StudentTest
import com.pankaj.consagous.interfaces.TestStudentClickListener


class TestStudentAdapter(private val testList:ArrayList<StudentTest>, val context:Context, val listener : TestStudentClickListener) : RecyclerView.Adapter<TestStudentAdapter.ViewHolder>() {



    class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName : TextView = itemView.findViewById<TextView>(R.id.tv_name)
        val tvRollNo : TextView = itemView.findViewById<TextView>(R.id.tv_roll_no)
        val tvEmail : TextView = itemView.findViewById<TextView>(R.id.tv_email)
        val tvMarks :TextView = itemView.findViewById(R.id.tv_total_mark)
        val imgEdit :ImageView = itemView.findViewById(R.id.img_edit_mark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.test_student_list_item,parent,false);
       return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val test = testList[position]

        holder.tvName.text = "Name :"+test.name
        holder.tvRollNo.text = "Roll No : "+test.rollNo
        holder.tvEmail.text = "Email : "+test.email
        holder.tvMarks.text = "Marks Obtained : "+test.markObtained.toString()
        holder.imgEdit.setOnClickListener(View.OnClickListener {
            listener.onEditClick(test,position)
        })

    }

    override fun getItemCount(): Int {
        return  testList.size
    }


}