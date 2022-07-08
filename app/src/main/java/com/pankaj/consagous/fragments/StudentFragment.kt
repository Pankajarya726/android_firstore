package com.pankaj.consagous.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pankaj.consagous.R
import com.pankaj.consagous.StudentItemClickListener
import com.pankaj.consagous.data_class.Student
import com.pankaj.consagous.activity.AddStudent
import com.pankaj.consagous.activity.StudentDetail
import com.pankaj.consagous.adapter.StudentListAdapter
import com.pankaj.consagous.databinding.FragmentStudentBinding
import com.pankaj.consagous.utils.FirebaseUtils

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentFragment : Fragment(),StudentItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var adapter: StudentListAdapter? = null

    private var studentList = ArrayList<Student>();

    private lateinit var binding: FragmentStudentBinding

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StudentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            StudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_student, container, false)

        return binding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_student, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressCircular.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.rvStudents.visibility = View.GONE
        init()
        binding.studentFabBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, AddStudent::class.java);
            startActivity(intent)
        })

    }

    private fun init() {


        FirebaseUtils().fireStoreDatabase.collection("student_db")
            .get()
            .addOnSuccessListener { result ->

                Log.e("UserDownload", " Size " + result.size())
                studentList.clear()
                for (document in result) {
                    Log.d("UserDownload", "${document.id} => ${document.data}")
                    studentList.add(
                        Student(
                            Integer.parseInt(document.data["roll_no"].toString()),
                            document.data["name"].toString(),
                            document.data["email"].toString(),
                            document.data["class"].toString(),
                        )
                    )
                }

                setUpAdapter(studentList)
            }
            .addOnFailureListener { exception ->
                Log.e("UserDownload", "Error getting documents.", exception)
            }




    }

    private fun setUpAdapter(studentList: java.util.ArrayList<Student>) {
        Log.e(javaClass.simpleName, " student list size " + studentList.size)


        if (studentList.isNotEmpty()) {
            binding.progressCircular.visibility = View.GONE
            binding.tvError.visibility = View.GONE
            binding.rvStudents.visibility = View.VISIBLE

            val layoutManager = LinearLayoutManager(activity)
            binding.rvStudents.layoutManager = layoutManager
            adapter = StudentListAdapter(studentList, activity!!.applicationContext,this)
            binding.rvStudents.adapter = adapter
            adapter!!.notifyDataSetChanged()


        }
    }


    override fun onResume() {
        super.onResume()
        binding.progressCircular.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.rvStudents.visibility = View.GONE
        studentList.clear();
        init()

        Log.e(javaClass.simpleName, " onResume---> ")
    }

    override fun onItemClick(student: Student) {
        val intent  = Intent(activity,StudentDetail::class.java);
        intent.putExtra("roll_no",student.rollNo);
        intent.putExtra("name",student.name);
        startActivity(intent)
    }


}