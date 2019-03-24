package com.example.bmi

import com.example.bmi.logic.BmiForKgM
import com.example.bmi.logic.BmiForLbIn
import com.example.bmi.logic.BmiRange
import org.junit.Assert
import org.junit.Test

class LogicTests {


    // zeby metoda byla testem musi miec adnotacje @Test (w JUnit test 4.0 at least) jak nie to jest zwykla metoda
    @Test
    fun forValidDataShouldReturnBmiKg () {
        val bmi = BmiForKgM(65, 170)
        Assert.assertEquals(22.491, bmi.countBmi(), 0.001) // delta - przymowac o rzad wiecej niz ten rzad, ktorego planujemy wykorzystac w programie
    }

    @Test
    fun myBmiValidKg () {
        val bmi = BmiForKgM(87, 183)
        Assert.assertEquals(25.979, bmi.countBmi(), 0.001)
    }

    //
    @Test
    fun forValidDataShouldReturnBmiLb () {
        val bmi = BmiForLbIn(100, 50)
        Assert.assertEquals(28.12, bmi.countBmi(), 0.001) // delta - przymowac o rzad wiecej niz ten rzad, ktorego planujemy wykorzystac w programie
    }

    @Test
    fun myBmiValidLb () {
        val bmi = BmiForLbIn(150, 67)
        Assert.assertEquals(23.5, bmi.countBmi(), 0.01)
    }

    //check range
    @Test
    fun checkRange(){
        val names = listOf("underweight","normal","overweight","obesity","s_obesity")
        val values = listOf(12.0, 22.0, 27.0, 33.0, 37.0)

        for ((n,v) in names.zip(values)) {
            val bmiRange = BmiRange(v)
            Assert.assertEquals(n, bmiRange.returnRange())
        }
    }

}