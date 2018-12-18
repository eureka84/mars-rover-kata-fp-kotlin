package marsroverkata

import marsroverkata.Data.Command
import marsroverkata.Data.Command.*
import marsroverkata.Data.Planet
import marsroverkata.Data.Position

object DataParsers {

    val parsePlanet: (String) -> Planet = { s: String ->
        val tokens = s.split("x")
        Planet(tokens[0].toInt(), tokens[1].toInt())
    }

    val parseObstacles: (String) -> List<Position> = { s: String ->
        if (s.isEmpty()) listOf()
        else s.split("/").asSequence().map(parsePosition).toList()
    }

    val parsePosition: (String) -> Position = { s: String ->
        val tokens = s.split(",")
        Position(tokens[0].toInt(), tokens[1].toInt())
    }

    val parseCommands: (String) -> List<Command> = { s: String -> s.map(parseCommand).toList() }

    private val parseCommand: (Char) -> Command = { c: Char ->
        when (c) {
            'l' -> TurnLeft
            'r' -> TurnRight
            'f' -> MoveForward
            'b' -> MoveBackward
            else -> UnknownCommand
        }
    }

}