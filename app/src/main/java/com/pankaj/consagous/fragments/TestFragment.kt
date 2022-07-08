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
import com.pankaj.consagous.activity.AddTest
import com.pankaj.consagous.adapter.StudentListAdapter
import com.pankaj.consagous.adapter.TestListAdapter
import com.pankaj.consagous.data_class.Test
import com.pankaj.consagous.databinding.FragmentTestBinding
import com.pankaj.consagous.utils.FirebaseUtils

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentTestBinding
    private var adapter: TestListAdapter? = null
    private var testList = ArrayList<Test>()

    companion object {
        @JvmStatic
        fun newInstance() =
            TestFragment().apply {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressCircular.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.rvTest.visibility = View.GONE
        init()
        binding.testFabBtn.setOnClickListener(View.OnClickListener {

            val intent = Intent(activity, AddTest::class.java)
            startActivity(intent)

        })

    }

    private fun init() {
        FirebaseUtils().fireStoreDatabase.collection("test")
            .get()
            .addOnSuccessListener { result ->
                Log.e("Test", " Size " + result.size())
                testList.clear()
                for (document in result) {
                    Log.d("Test", "${document.id} => ${document.data}")
                    testList.add(
                        Test(
                            Integer.parseInt(document.data["test_no"].toString()),
                            document.data["date"].toString(),
                            document.data["class"].toString(),
                            document.data["subject"].toString(),
                            document.data["topic"].toString(),
                            Integer.parseInt(document.data["max_mark"].toString()),
                        )
                    )
                }

                setUpAdapter(testList)
            }
            .addOnFailureListener { exception ->
                Log.e("Test", "Error getting documents.", exception)
            }

    }

    private fun setUpAdapter(testList: java.util.ArrayList<Test>) {
        Log.e(javaClass.simpleName, " student list size " + testList.size)


        if (testList.isNotEmpty()) {
            binding.progressCircular.visibility = View.GONE
            binding.tvError.visibility = View.GONE
            binding.rvTest.visibility = View.VISIBLE

            val layoutManager = LinearLayoutManager(activity)
            binding.rvTest.layoutManager = layoutManager
            adapter = TestListAdapter(testList, activity!!.applicationContext)
            binding.rvTest.adapter = adapter
            adapter!!.notifyDataSetChanged()


        }
    }


    override fun onResume() {
        super.onResume()
        binding.progressCircular.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.rvTest.visibility = View.GONE
        testList.clear();
        init()

        Log.e(javaClass.simpleName, " onResume---> ")
    }
}