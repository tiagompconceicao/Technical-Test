package tiago.cognizant.technicaltest.model

import java.lang.StringBuilder
import kotlin.random.Random

class URL() {
    private var url: String

    init {
        url = generateURL()
    }

    fun getURL(): String {
        return url
    }


    private fun generateURL(): String{
        val str = StringBuilder()
        for (i in 0..Random.nextInt(5,10)){
            str.append(Random.nextInt(97,122).toChar())
        }
        str.append(".com")
        return str.toString()
    }
}