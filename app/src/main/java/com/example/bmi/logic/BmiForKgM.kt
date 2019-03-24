package com.example.bmi.logic

import logic.Bmi
import java.lang.IllegalArgumentException

class BmiForKgM(var mass: Int, var height: Int) : Bmi {

    override fun countBmi(): Double{
        require(mass > 20 && height > 50){ "Niemożliwe dane"}
        val bmi : Double = 10000.0 * mass / (height * height)
        return bmi
    }
}