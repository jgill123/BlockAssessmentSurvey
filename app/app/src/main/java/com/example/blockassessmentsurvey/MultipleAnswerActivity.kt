package com.example.blockassessmentsurvey


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator

private lateinit var checkBoxContainer : RadioGroup

class MultipleAnswerActivity : AppCompatActivity() {

    private var questionText: TextView? = null
    private var doneBtn: ImageButton? = null
    private var progressBar: ProgressBar? = null
    private var submitButton: Button? = null
    private var backBtn: ImageButton? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_answer)

        val toAsk = intent.getStringExtra(QUESTION_STRING)

        questionText = findViewById(R.id.QuestionText)

        questionText?.text = toAsk

        val questions = intent.getStringExtra(QANSWER_STRING)

        progressBar = findViewById(R.id.progressBar3)

        val questionProgress = intent.getStringExtra(PROGRESS_STRING)
        progressBar!!.setProgress(questionProgress.toInt())

        checkBoxContainer = findViewById<RadioGroup>(R.id.radioGroup2)

        for (option in questions.split(",")){

            val toAdd = CheckBox(this)
            toAdd.id = View.generateViewId()
            //toAdd.gravity = Gravity.CENTER

            toAdd.text = option
            if(option.contentEquals("Other")){
                toAdd.setOnCheckedChangeListener {b, bool -> displayTextBox() }
            }
            Log.i(TAG,option)



            checkBoxContainer.addView(toAdd)
        }

        submitButton = findViewById(R.id.submit)

        submitButton?.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        doneBtn = findViewById(R.id.next)

        doneBtn?.setOnClickListener{ submit() }

        backBtn?.setOnClickListener {
            val data = Intent()

            //put the question id in the intent
            data.putExtra(QID_STRING, intent.getStringExtra(QID_STRING))

            setResult(Activity.RESULT_CANCELED, data)
            finish()
        }



    }

    private fun displayTextBox(){
        val textBox = findViewById<TextView>(R.id.editText)
        if (textBox.isShown){
            textBox.setVisibility(View.INVISIBLE)
        }
        else{
            textBox.setVisibility(View.VISIBLE)
        }
        return
    }

    private fun submit() {

        val answers = mutableListOf<String>()
        val textBox = findViewById<TextView>(R.id.editText)

        //gets the selected radio buttons text
        for (checkBox in checkBoxContainer.iterator()) {
            val checkedAns= findViewById<CheckBox>(checkBox.id)
            if(checkedAns.isChecked() && !checkedAns.text.equals("Other")){
                answers.add(checkedAns.text.toString())
            }
        }
        //val ans = findViewById<Chip>(chipContainer.checkedChipId)
        if(textBox.isShown){
            if(textBox.text.isBlank()){
                Toast.makeText(applicationContext, "Please describe other to continue.", Toast.LENGTH_LONG).show()
                return
            }
            answers.add("Other: " + textBox.text.toString())
        }

        if(answers.isEmpty()) {
            Toast.makeText(applicationContext, "Please select an answer to continue.", Toast.LENGTH_LONG).show()
            return
        }
        else {

            val data = Intent()

            for(ans in answers) {
                // puts the ans text into the intent
                data.putExtra(QANSWER_STRING, ans)

                //put the question id in the intent
                data.putExtra(QID_STRING, intent.getStringExtra(QID_STRING))
                Log.i(TAG, ans)
            }

            setResult(Activity.RESULT_OK, data)
            finish()


            return
        }
    }


    companion object {
        private val TAG = "BAS-MultipleChoice"
        private val QUESTION_STRING = "questionstring"
        private val QANSWER_STRING = "qanswer"
        private val QID_STRING = "qid"
        private val PROGRESS_STRING = "progess"
    }

}
