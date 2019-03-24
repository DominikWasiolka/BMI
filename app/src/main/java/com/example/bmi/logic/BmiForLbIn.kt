package com.example.bmi.logic

import logic.Bmi

class BmiForLbIn(var mass: Int, var height: Int) : Bmi {

    override fun countBmi(): Double{
        require(mass > 40 && height > 20){ "Niemożliwe dane"}
        val bmi : Double = 703.0 * mass / (height * height)
        return bmi
    }
}