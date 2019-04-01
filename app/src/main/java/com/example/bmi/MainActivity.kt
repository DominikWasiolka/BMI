package com.example.bmi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.bmi.logic.BmiForKgM
import com.example.bmi.logic.BmiForLbIn
import com.example.bmi.logic.BmiRange
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import logic.Bmi
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    var isUnitsKg : Boolean = true
    var last_bmi: Double = 0.0
    //var prevResList:Queue<Result> = LinkedList <Result>()
    var prevResList:Queue<Result> = LinkedList <Result>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById(R.id.button) as Button
        val info_button = findViewById(R.id.info_button) as ImageButton

        //Get previous BMI Results
        prevResList = getResults()

        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?){
                var mass: String
                var height: String
                mass = (findViewById(R.id.weight_edit_field) as EditText).getText().toString()
                height = (findViewById(R.id.height_edit_field) as EditText).getText().toString()

                val iMass:Int
                val iHeight:Int
                try{
                    iMass = Integer.parseInt(mass)
                    iHeight = Integer.parseInt(height)
                } catch (e: NumberFormatException){
                    Toast.makeText(applicationContext, getString(R.string.empty_err_msg), Toast.LENGTH_SHORT).show()
                    return
                }

                val bmiCalc: Bmi
                if (isUnitsKg)
                    bmiCalc = BmiForKgM(iMass, iHeight)
                else
                    bmiCalc = BmiForLbIn(iMass, iHeight)

                val bmiRes:Double
                try {
                    bmiRes = bmiCalc.countBmi()
                } catch (e: IllegalArgumentException){
                    Toast.makeText(applicationContext, getString(R.string.wrong_input_err_msg), Toast.LENGTH_SHORT).show()
                    return
                }
                last_bmi = bmiRes
                val newFormat = DecimalFormat("#.##")
                val bmiResRounded: Double = java.lang.Double.valueOf(newFormat.format(bmiRes))

                val bmiRange = BmiRange(bmiRes)

                val bmiName = bmiRange.returnRange()
                val bmiColor = getColor(bmiName)
                val bmiFullName = getString(bmiName)

                (findViewById(R.id.bmi_status) as TextView).setText(bmiFullName)
                (findViewById(R.id.bmiResult) as TextView).setTextColor(bmiColor)
                (findViewById(R.id.bmiResult) as TextView).setText(bmiResRounded.toString())

                addResult(iHeight, iMass, bmiResRounded, bmiColor)
            }
        })

        info_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (last_bmi>0.0)
                    openInfoActivity(last_bmi)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.about_button -> {
                //Toast.makeText(this, "item 1 is Selcted", Toast.LENGTH_SHORT).show()
                openAboutActivity()
                return super.onOptionsItemSelected(item)
            }

            R.id.ch_units -> {
                if (isUnitsKg){
                    isUnitsKg = false
                    Toast.makeText(this, getString(R.string.unit2lb), Toast.LENGTH_SHORT).show()
                    (findViewById(R.id.weightTextField) as TextView).setText(getString(R.string.weightLb))
                    (findViewById(R.id.heightTextField) as TextView).setText(getString(R.string.heightIn))
                    clearFields()
                }
                else{
                    isUnitsKg = true
                    Toast.makeText(this, getString(R.string.unit2kg), Toast.LENGTH_SHORT).show()
                    (findViewById(R.id.weightTextField) as TextView).setText(getString(R.string.weightKg))
                    (findViewById(R.id.heightTextField) as TextView).setText(getString(R.string.heightCm))
                    clearFields()
                }

                return super.onOptionsItemSelected(item)
            }
            R.id.previous -> {
                //Toast.makeText(this, "item 1 is Selcted", Toast.LENGTH_SHORT).show()
                openPrevResultsActivity()
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val bmiStatus = bmi_status.text
        val bmiVal = bmiResult.text
        val bmiColor = bmiResult.currentTextColor
        outState?.putCharSequence("BMI_STATUS",bmiStatus)
        outState?.putCharSequence("BMI_TEXT",bmiVal)
        outState?.putInt("BMI_COLOR", bmiColor)
        outState?.putBoolean("UNITS", isUnitsKg)
        outState?.putDouble("BMI_VAL",last_bmi)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        val bmiStatus = savedInstanceState?.getCharSequence("BMI_STATUS")
        val bmiResult = savedInstanceState?.getCharSequence("BMI_TEXT")
        val bmiColor = savedInstanceState?.getInt("BMI_COLOR")
        val unitKg = savedInstanceState?.getBoolean("UNITS")
        last_bmi = savedInstanceState?.getDouble("BMI_VAL")!!
        (findViewById(R.id.bmi_status) as TextView).setText(bmiStatus)
        (findViewById(R.id.bmiResult) as TextView).setText(bmiResult)
        (findViewById(R.id.bmiResult) as TextView).setTextColor(bmiColor!!)

        if (unitKg == false){
            (findViewById(R.id.weightTextField) as TextView).setText(getString(R.string.weightLb))
            (findViewById(R.id.heightTextField) as TextView).setText(getString(R.string.heightIn))
            isUnitsKg = unitKg
        }
    }

    fun openAboutActivity(){
        val intent = Intent(this,AboutActivity::class.java)
        startActivity(intent)
    }
    fun openInfoActivity(bmi_value: Double){
        val intent = Intent(this,InfoActivity::class.java)
        intent.putExtra(getString(R.string.bmi_value_key),bmi_value)
        startActivity(intent)
    }
    fun openPrevResultsActivity() {
        val intent = Intent(this, PreviousBMIs::class.java)
        val resultType = object : TypeToken<LinkedList<Result>>() {}.type
        val jsonResults = Gson().toJson(this.prevResList, resultType)
        intent.putExtra("results", jsonResults)
        startActivity(intent)
    }

    fun clearFields(){
        last_bmi = 0.0
        (findViewById(R.id.bmiResult) as TextView).setText("")
        (findViewById(R.id.weight_edit_field) as EditText).setText("")
        (findViewById(R.id.height_edit_field) as EditText).setText("")
    }

    fun getString(name: String): String {
        return resources.getString(resources.getIdentifier(name, "string", packageName))
    }
    fun getColor(name: String): Int {
        return resources.getColor(resources.getIdentifier(name, "color", packageName))
    }

    private fun addResult(height:Int, weight:Int, bmi:Double, color: Int){
        val date = LocalDateTime.now()
        var formatter = DateTimeFormatter.ofPattern(getString(R.string.date_pattern))
        val date_str = formatter.format(date)
        var system = getString(R.string.lb_system)
        if (isUnitsKg)
            system =getString(R.string.kg_system)


        val result = Result(height.toString(), weight.toString(),bmi.toString(), date_str, system, color)
        if (this.prevResList.size<10)
            this.prevResList.add(result)
        else{
            this.prevResList.poll()
            this.prevResList.add(result)
        }

        saveResults()
    }

    fun saveResults(){
        val resultType = object : TypeToken<LinkedList<Result>>() {}.type
        val prefEditor = PreferenceManager.getDefaultSharedPreferences(this).edit()
        val jsonString = Gson().toJson(this.prevResList,resultType)
        prefEditor.putString("results", jsonString).apply()
    }

    fun getResults() : LinkedList<Result>{
        val resultType = object : TypeToken<LinkedList<Result>>() {}.type
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val jsonString = preferences.getString("results",null)

        return if(jsonString != null)
            Gson().fromJson(jsonString, resultType)
        else
            LinkedList<Result>()
    }

}

class Result(val height:String, val weight:String, val bmi:String, val date:String, val system:String, val color:Int){
}
//TODO add mode (ibs/inch or kg/cm)