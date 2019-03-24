package com.example.bmi

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.bmi.logic.BmiForKgM
import com.example.bmi.logic.BmiForLbIn
import com.example.bmi.logic.BmiRange
import kotlinx.android.synthetic.main.activity_main.*
import logic.Bmi
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    var isUnitsKg : Boolean = true
    var last_bmi: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val button = findViewById(R.id.button) as Button
        val info_button = findViewById(R.id.info_button) as ImageButton

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
}
