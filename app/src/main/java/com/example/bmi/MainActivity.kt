package com.example.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.bmi.logic.BmiForKgM
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var mass: String
        var height: String
        val button = findViewById(R.id.button) as Button

        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?){
                mass = (findViewById(R.id.editText) as EditText).getText().toString()
                height = (findViewById(R.id.editText2) as EditText).getText().toString()

                val iMass = Integer.parseInt(mass)
                val iHeight = Integer.parseInt(height)
                val bmiCalc = BmiForKgM(iMass, iHeight)
                val bmiRes = bmiCalc.countBmi()

                val newFormat = DecimalFormat("#.##")
                val bmiResRounded: Double = java.lang.Double.valueOf(newFormat.format(bmiRes))

                System.out.println(mass)
                System.out.println(height)

                (findViewById(R.id.bmiResult) as TextView).setText("Twoje BMI wynosi: \n "+bmiResRounded.toString())
            }
        })


    }
}
