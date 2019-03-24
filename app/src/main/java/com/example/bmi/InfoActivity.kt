package com.example.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.bmi.logic.BmiRange
import java.text.DecimalFormat

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val BMI_VALUE: Double = intent.extras.getDouble(getString(R.string.bmi_value_key))
        val bmiRange = BmiRange(BMI_VALUE)
        val bmiName = bmiRange.returnRange()

        val newFormat = DecimalFormat("#.##")
        val bmiResRounded: Double = java.lang.Double.valueOf(newFormat.format(BMI_VALUE))
        (findViewById(R.id.bmi_info_result) as TextView).setText(bmiResRounded.toString())

        val bmiText = getString(bmiName+"Text")

        (findViewById(R.id.longText) as TextView).setText(bmiText)
    }
    fun getString(name: String): String {
        return resources.getString(resources.getIdentifier(name, "string", packageName))
    }
}
