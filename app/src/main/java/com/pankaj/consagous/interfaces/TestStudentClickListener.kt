package com.pankaj.consagous.interfaces

import com.pankaj.consagous.data_class.StudentTest

interface TestStudentClickListener {

    fun onEditClick(test: StudentTest, position: Int)
}