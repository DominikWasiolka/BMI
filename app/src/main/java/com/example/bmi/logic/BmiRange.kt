package com.example.bmi.logic

import com.example.bmi.R

class BmiRange(var bmiValue: Double) {

    fun returnRange() : String{
        val color = if (bmiValue< 18.5)
            return "underweight"
        else if (bmiValue<25)
            return "normal"
        else if (bmiValue<30)
            return "overweight"
        else if (bmiValue<35)
            return "obesity"
        else
            return "s_obesity"
    }
}