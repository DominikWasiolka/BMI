package com.example.bmi

import com.example.bmi.logic.BmiForKgM
import io.kotlintest.TestContext
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import org.junit.Assert
import java.lang.IllegalArgumentException


class MyAnotherTests : StringSpec() {
    init{
        "MOj pierwszy test " {
        val bmi = BmiForKgM( 65, 170)
        bmi.countBmi() shouldBeAround 22.491 // shouldBe jest infixem, więc nie trzeba dawać kropek i nawiasów
        }

        "for mass or height lower then 0 should throw exception"{
            val bmi = BmiForKgM ( -1 , 0)
            shouldThrow<IllegalArgumentException> {
                bmi.countBmi()
            }
        }

    }

    infix fun Double.shouldBeAround(value : Double){
        Assert.assertEquals(value, this, 0.001)
    }
}