package marsroverkata

import arrow.effects.IO

object IoOps {
    fun ask(question: String) =
            puts(question).flatMap { reads() }

    fun puts(str: String): IO<Unit> = IO {
        println(str)
    }

    private fun reads(): IO<String> = IO {
        readLine()!!
    }
}