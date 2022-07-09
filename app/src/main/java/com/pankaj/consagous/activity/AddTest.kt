package com.pankaj.consagous.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.pankaj.consagous.R
import com.pankaj.consagous.databinding.ActivityAddTestBinding
import com.pankaj.consagous.utils.FirebaseUtils
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class AddTest : AppCompatActivity() {
    private var student = ArrayList<Int>()
    private lateinit var binding: ActivityAddTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_test)


        val mcurrentTime = Calendar.getInstance()
        val year = mcurrentTime.get(Calendar.YEAR)
        val month = mcurrentTime.get(Calendar.MONTH)
        val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this,

            { view, year, month, dayOfMonth ->
                binding.edtDate.setText(
                    String.format("%d-%d-%d", dayOfMonth, month + 1, year)
                )
            }, year, month, day
        );


        binding.edtDate.setOnClickListener {
            datePicker.show()
        }


        binding.btnAdd.setOnClickListener(View.OnClickListener {
            addTest()
        })

        binding.imgBack.setOnClickListener(
            View.OnClickListener {
                finish()
            }
        )

    }

    private fun init() {
        FirebaseUtils().fireStoreDatabase.collection("students")
            .get()
            .addOnSuccessListener { result ->

                Log.e("UserDownload", " Size " + result.size())

                for (document in result) {
                    Log.d("UserDownload", "${document.id} => ${document.data}")
                    student.add(
                        Integer.parseInt(document.data["roll_no"].toString())
                    )
                }


            }
            .addOnFailureListener { exception ->
                Log.e("UserDownload", "Error getting documents.", exception)
            }

    }


    private fun addTest() {

        if (binding.edtClass.text.trim().isEmpty()) {
            Toast.makeText(this, "Please enter class", Toast.LENGTH_SHORT).show()
        } else if (binding.edtDate.text.trim().isEmpty()) {
            Toast.makeText(this, "Please enter date", Toast.LENGTH_SHORT).show()
        } else if (binding.edtSubject.text.trim().isEmpty()) {
            Toast.makeText(this, "Please enter subject", Toast.LENGTH_SHORT).show()
        } else if (binding.edtTopic.text.trim().isEmpty()) {
            Toast.makeText(this, "Please enter topic", Toast.LENGTH_SHORT).show()
        } else if (binding.edtMarks.text.trim().isEmpty()) {
            Toast.makeText(this, "Please enter max mark", Toast.LENGTH_SHORT).show()
        } else {
            val timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
            val classs = binding.edtClass.text.trim().toString();
            val date = binding.edtDate.text.trim().toString();
            val subject = binding.edtSubject.text.trim().toString();
            val topic = binding.edtTopic.text.trim().toString();
            val maxMark = binding.edtMarks.text.trim().toString();

            // create a dummy data
            val hashMap = hashMapOf<String, Any>(
                "class" to classs,
                "test_no" to timeStamp,
                "date" to date,
                "subject" to subject,
                "topic" to topic,
                "max_mark" to maxMark
            )
            val mFirestore = FirebaseFirestore.getInstance()
            mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            FirebaseUtils().fireStoreDatabase.collection("test")
                .add(hashMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Test added successfully", Toast.LENGTH_LONG).show()
                    Log.d(javaClass.simpleName, "Added Test with ID ${it.id}")
                    addStudentInTest(it.id, timeStamp, classs);
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error : ${exception.toString()}", Toast.LENGTH_LONG)
                        .show()
                    Log.w(javaClass.simpleName, "Error adding document $exception")
                }

        }


    }

    private fun addStudentInTest(id: String, timeStamp: Long, classs: String) {
        FirebaseUtils().fireStoreDatabase.collection("students").whereEqualTo("class", classs)
            .get().addOnSuccessListener { result ->
            for (document in result) {
                Log.d("UserDownload", "${document.id} => ${document.data}")
                student.add(
                    Integer.parseInt(document.data["roll_no"].toString())
                )
            }
            student.forEach {
                val hashMap = hashMapOf<String, Any>(
                    "roll_no" to it,
                    "test_no" to timeStamp,
                    "mark_obtained" to 0,
                )
                FirebaseUtils().fireStoreDatabase.collection("student_test")
                    .add(hashMap).addOnSuccessListener {
                        Log.d(javaClass.simpleName, "Added Test with ID ${it.id}")
                    }
                    .addOnFailureListener { exception ->
                        Log.w(javaClass.simpleName, "Error adding document $exception")
                    }
            }
            finish()

        }
    }
}