package study.android.room2


import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var rbStudent: RadioButton
    private lateinit var rbSubject: RadioButton
    private lateinit var spinner: Spinner
    private lateinit var listCaption: TextView
    private lateinit var recyclerView: RecyclerView



    val db by lazy {
        Room.databaseBuilder(
            this,
            SchoolDatabase::class.java, "school.db"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rbStudent = findViewById(R.id.rbStudent)
        rbSubject = findViewById(R.id.rbSubject)
        spinner = findViewById(R.id.spinner)
        listCaption = findViewById(R.id.listCaption)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        rbStudent.setOnClickListener{
            listCaption.text = "Student's subjects"
            GlobalScope.launch {
                val s = db.schoolDao.getStudents()
                val st = MutableList(0){""}
                for (i in s)
                    st.add(i.studentName)
                withContext(Main){
                    spinner.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, st )
                }
            }
        }

        rbSubject.setOnClickListener{
            listCaption.text = "Students study"
            GlobalScope.launch {
                val s = db.schoolDao.getSubjects()
                val st = MutableList(0){""}
                for (i in s)
                    st.add(i.subjectName)
                withContext(Main){
                    spinner.adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, st )
                }
            }
        }

        val items = arrayOf("Здесь", "должен", "располагаться", "список", "учеников", "или предметов")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, items
        )

        spinner.adapter = adapter


        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?,
                position: Int, id: Long
            ) {

                GlobalScope.launch {
                    if (rbStudent.isChecked) {
                        val s = db.schoolDao.getSubjectsOfStudent(
                            spinner.adapter.getItem(position).toString()
                        )
                        val st = MutableList(0) { "" }
                        for (i in s[0].subjects)
                            st.add(i.subjectName)
                        withContext(Main) {
                            recyclerView.adapter = MyAdapter(st)
                        }
                    }
                    if(rbSubject.isChecked) {
                        val s = db.schoolDao.getStudentsOfSubject(
                            spinner.adapter.getItem(position).toString()
                        )
                        val st = MutableList(0) { "" }
                        for (i in s[0].students)
                            st.add(i.studentName)
                        withContext(Main) {
                            recyclerView.adapter = MyAdapter(st)
                        }
                    }

                }
            }



            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }

        val dao = db.schoolDao;

        lifecycleScope.launch {
            DataExample.directors.forEach { dao.insertDirector(it) }
            DataExample.schools.forEach { dao.insertSchool(it) }
            DataExample.subjects.forEach { dao.insertSubject(it) }
            DataExample.students.forEach { dao.insertStudent(it) }
            DataExample.studentSubjectRelations.forEach { dao.insertStudentSubjectCrossRef(it) }
        }
    }

}