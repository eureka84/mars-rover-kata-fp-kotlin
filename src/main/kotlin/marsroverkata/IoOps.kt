package marsroverkata

import arrow.effects.IO

object IoOps {

    val ask: (String) -> IO<String> = { question -> puts(question).flatMap(reads) }

    val puts: (String) -> IO<Unit> = { str -> IO { println(str) } }

    private val reads: (Unit) -> IO<String> = { IO { readLine()!! } }
}