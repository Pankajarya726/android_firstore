package com.pankaj.consagous.activity

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.pankaj.consagous.R
import com.pankaj.consagous.adapter.StudentTestAdapter
import com.pankaj.consagous.data_class.StudentTest
import com.pankaj.consagous.databinding.ActivityStudentDetailBinding
import com.pankaj.consagous.utils.FirebaseUtils
import kotlinx.coroutines.tasks.await

class StudentDetail : AppCompatActivity() {

    lateinit var binding: ActivityStudentDetailBinding
    private var adapter : StudentTestAdapter?=null

    private var tests = ArrayList<StudentTest>()
    var rollNo: Int = 0
    var name: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil. setContentView(this,R.layout.activity_student_detail)
        rollNo = intent.getIntExtra("roll_no", 0)
        name = intent.getStringExtra("name").toString()
        Log.e(javaClass.simpleName, "rollNo---$rollNo")
        Log.e(javaClass.simpleName, "name---$name")
        binding.tvTitle.text = name
        binding.rvStudentTest.visibility = View.GONE
        binding.indicator.visibility = View.VISIBLE
        getStudentDetail(rollNo)
        Log.e(javaClass.simpleName, "subject-->$tests");
        binding.imgBack.setOnClickListener(View.OnClickListener {

            finish()
        })

    }

    private fun initView() {

        binding.rvStudentTest.layoutManager = LinearLayoutManager(this)
        adapter = StudentTestAdapter(testList = tests,this)
        binding.rvStudentTest.adapter = adapter


    }

    private fun getStudentDetail(rollNo: Int) {


        FirebaseUtils().fireStoreDatabase.collection("student_test").whereEqualTo("roll_no", rollNo).get()
            .addOnSuccessListener { result ->

                val testList = ArrayList<Int>()
                Log.e(javaClass.simpleName, "result-->" + result.size());

                for (document in result) {

                    FirebaseUtils().fireStoreDatabase.collection("test").whereEqualTo(
                        "test_no", Integer.parseInt(
                            document.data["test_no"].toString
                                ()
                        )
                    ).get().addOnSuccessListener {

                        tests.add(StudentTest(
                            rollNo,
                            name,
                            "",
                            it.documents[0]["class"].toString(),
                            Integer.parseInt(it.documents[0]["test_no"]
                            .toString()),
                            it.documents[0]["date"].toString(),
                            it.documents[0]["subject"].toString(),
                            it.documents[0]["topic"].toString(),
                            Integer.parseInt(it.documents[0]["max_mark"].toString()),
                            Integer.parseInt(document["mark_obtained"].toString()),
                            document.id
                            ))
                        Log.e(javaClass.simpleName, "subject-->" + it.documents[0]["subject"]);

                    }

                }


                Log.e(javaClass.simpleName, "subject-->$tests");



            }.addOnFailureListener { exception ->
                Log.w(javaClass.simpleName, "Error adding document $exception")
            }

        Handler().postDelayed({
            binding.rvStudentTest.visibility = View.VISIBLE
            binding.indicator.visibility = View.GONE
            initView()
        }, 3000)


    }
}