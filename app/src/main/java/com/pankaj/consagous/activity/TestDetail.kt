package com.pankaj.consagous.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.pankaj.consagous.R
import com.pankaj.consagous.adapter.TestStudentAdapter
import com.pankaj.consagous.data_class.StudentTest
import com.pankaj.consagous.databinding.ActivityTestDetailBinding
import com.pankaj.consagous.interfaces.TestStudentClickListener
import com.pankaj.consagous.utils.FirebaseUtils
import java.lang.Exception

class TestDetail : AppCompatActivity(), TestStudentClickListener {

    private var studentList = ArrayList<StudentTest>()
    private var adapter: TestStudentAdapter? = null
    private lateinit var binding: ActivityTestDetailBinding
    private var testNo: Int = 0
    private var subject: String = ""
    private var topic: String = ""
    private var classs: String = ""
    private var date: String = ""
    private var maxMark: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_detail)

        testNo = intent.getIntExtra("test_no", 0)
        subject = intent.getStringExtra("subject").toString()
        topic = intent.getStringExtra("topic").toString()
        maxMark = intent.getIntExtra("max_mark", 0)
        classs = intent.getStringExtra("class").toString()
        date = intent.getStringExtra("date").toString()

        binding.tvTitle.text = subject + " " + classs
        binding.tvDate.text = "Date : " + date
        binding.tvTopic.text = "Topic : " + topic
        binding.tvMaxMark.text = "Max mark : " + maxMark.toString()


        getStudents(testNo)
        binding.imgBack.setOnClickListener(View.OnClickListener {
            finish();
        })


    }

    private fun getStudents(testNo: Int) {

        try {

            FirebaseUtils().fireStoreDatabase.collection("student_test")
                .whereEqualTo("test_no", testNo).get().addOnSuccessListener { result ->


                    result.forEach { doc ->


                        FirebaseUtils().fireStoreDatabase.collection("students").whereEqualTo(
                            "roll_no", Integer.parseInt(
                                doc.data["roll_no"].toString
                                    ()
                            )
                        ).get().addOnSuccessListener {

                            studentList.add(
                                StudentTest(
                                    Integer.parseInt(
                                        doc.data["roll_no"].toString
                                            ()
                                    ),
                                    it.documents[0]["name"].toString(),
                                    it.documents[0]["email"].toString(),
                                    it.documents[0]["class"].toString(),
                                    testNo,
                                    date,
                                    subject,
                                    topic,
                                    maxMark,
                                    Integer.parseInt(doc["mark_obtained"].toString()),
                                    doc.id
                                )
                            )
                            Log.e(javaClass.simpleName, "subject-->" + it.documents[0]["name"]);

                        }

                    }


                }

            Handler().postDelayed({

                Log.e(javaClass.simpleName, "student in test-->" + studentList)
                binding.rvTestStudent.visibility = View.VISIBLE
                binding.indicator.visibility = View.GONE
                initView()
            }, 3000)

        } catch (e: Exception) {

            Log.e(javaClass.simpleName, "exception-->" + e);
        }

    }

    private fun initView() {

        binding.rvTestStudent.layoutManager = LinearLayoutManager(this)
        adapter = TestStudentAdapter(testList = studentList, this, this)
        binding.rvTestStudent.adapter = adapter
    }

    override fun onEditClick(test: StudentTest, position: Int) {

        Log.e(javaClass.simpleName, "onEditClick -->$test")

        val builder = AlertDialog.Builder(this)
            .create()
        val view = layoutInflater.inflate(R.layout.edit_mark_alert, null)
        val editText: EditText = view.findViewById<EditText>(R.id.editText)


        val button = view.findViewById<Button>(R.id.btn_update)
        builder.setView(view)


        editText.setText(test.markObtained.toString())
        button.setOnClickListener {
            if (editText.text.trim().toString().isEmpty()) {
                Toast.makeText(this, "Please enter marks", Toast.LENGTH_LONG).show()
            } else if (Integer.parseInt(editText.text.trim().toString()) > test.maxMark) {
                Toast.makeText(
                    this,
                    "Obtained mark can no be greater then max marks, Maximum mark is ${test.maxMark}",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Log.e(javaClass.simpleName, "update marks");

                try {
                    FirebaseUtils().fireStoreDatabase.collection("student_test")
                        .document(test.testId)
                        .update("mark_obtained", Integer.parseInt(editText.text.trim().toString()))
                        .addOnSuccessListener {

                            Toast.makeText(this, "Marks Updated successfully", Toast.LENGTH_LONG)
                                .show()
                            studentList[position].markObtained =
                                Integer.parseInt(editText.text.trim().toString())
                            adapter!!.notifyItemChanged(position)
                            builder.dismiss()

                        }
                } catch (e: Exception) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                    builder.dismiss()
                }


            }
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()


//        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
//        val alert: AlertDialog = alertDialog.create()
//
//
//        val customLayout: View =
//            LayoutInflater.from(this).inflate(com.pankaj.consagous.R.layout.edit_mark_alert, null)
//
//        val editText: EditText = customLayout.findViewById<EditText>(R.id.editText)
//        editText.setText(test.markObtained.toString())
//        customLayout.findViewById<Button>(R.id.btn_update).setOnClickListener(View.OnClickListener {
//
//
//        })
//        alertDialog.setView(customLayout)
//        alert.setCanceledOnTouchOutside(false)
//        alert.show()
    }


}