package study.android.room2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import study.android.room2.entities.Director
import study.android.room2.entities.School
import study.android.room2.entities.Student
import study.android.room2.entities.Subject
import study.android.room2.entities.relations.StudentSubjectCrossRef
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val db by lazy {
        Room.databaseBuilder(
            this,
            SchoolDatabase::class.java, "school.db"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dao = db.schoolDao;

        lifecycleScope.launch {
            DataExample.directors.forEach { dao.insertDirector(it) }
            DataExample.schools.forEach { dao.insertSchool(it) }
            DataExample.subjects.forEach { dao.insertSubject(it) }
            DataExample.students.forEach { dao.insertStudent(it) }
            DataExample.studentSubjectRelations.forEach { dao.insertStudentSubjectCrossRef(it) }
            val schoolWithDirector = dao.getSchoolAndDirectorWithSchoolName("Kotlin School")
            val schoolWithStudents = dao.getSchoolWithStudents("Kotlin School")
        }
        }

}