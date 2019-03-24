package com.example.bmi

import com.example.bmi.logic.BmiForKgM
import org.junit.Assert
import org.junit.Test

class MyFirstTests {


    // zeby metoda byla testem musi miec adnotacje @Test (w JUnit test 4.0 at least) jak nie to jest zwykla metoda
    @Test
    fun forValidDataShouldReturnBmi () {
        val bmi = BmiForKgM(65, 170)
        Assert.assertEquals(22.491, bmi.countBmi(), 0.001) // delta - przymowac o rzad wiecej niz ten rzad, ktorego planujemy wykorzystac w programie
    }

    @Test
    fun myBmiValid () {
        val bmi = BmiForKgM(87, 183)
        Assert.assertEquals(25.979, bmi.countBmi(), 0.001)
    }

}