package com.pankaj.consagous.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.pankaj.consagous.R
import com.pankaj.consagous.databinding.ActivityAddStudentBinding
import com.pankaj.consagous.utils.FirebaseUtils
import java.util.concurrent.TimeUnit

class AddStudent : AppCompatActivity() {

    private lateinit var binding: ActivityAddStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_student)
        binding.toolbar.setTitle(R.string.add_student)

        binding.imgBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        binding.btnAdd.setOnClickListener(View.OnClickListener {
            addStudent()
        })


    }

    private fun addStudent() {

        if(binding.edtName.text.trim().isEmpty()){
            Toast.makeText(this,"Please enter name",Toast.LENGTH_SHORT).show()
        }else if(binding.edtClass.text.trim().isEmpty()){
            Toast.makeText(this,"Please enter class",Toast.LENGTH_SHORT).show()
        }else if(binding.edtEmail.text.trim().isEmpty()){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show()
        }else{
            val timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
            val name  = binding.edtName.text.trim().toString();
            val classs  = binding.edtClass.text.trim().toString();
            val email  = binding.edtEmail.text.trim().toString();

// create a dummy data
            val hashMap = hashMapOf<String, Any>(
                "roll_no" to timeStamp,
                "name" to name,
                "email" to email,
                "class" to classs
            )
            val mFirestore = FirebaseFirestore.getInstance()
            mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            FirebaseUtils().fireStoreDatabase.collection("students")
                .add(hashMap)
                .addOnSuccessListener {
                    Toast.makeText(this,"Student added successfully",Toast.LENGTH_LONG).show()
                    Log.d(javaClass.simpleName, "Added document with ID ${it.id}")
                    finish()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this,"Error : ${exception.toString()}",Toast.LENGTH_LONG).show()
                    Log.w(javaClass.simpleName, "Error adding document $exception")
                }

        }


    }


}