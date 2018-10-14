package marsroverkata

import marsroverkata.Models.Position
import marsroverkata.Models.Planet
import marsroverkata.Models.Command
import marsroverkata.Models.Command.MoveBackward
import marsroverkata.Models.Command.MoveForward
import marsroverkata.Models.Command.TurnLeft
import marsroverkata.Models.Command.TurnRight
import marsroverkata.Models.Command.UnknownCommand

object Parsers {

    fun parsePlanet(s: String): Planet {
        val tokens = s.split("x")
        return Planet(tokens[0].toInt(), tokens[1].toInt())
    }

    fun parseObstacles(s: String): List<Position> =
            if (s.isEmpty()) listOf()
            else s.split("/").asSequence().map(this::parsePosition).toList()

    fun parsePosition(s: String): Position {
        val tokens = s.split(",")
        return Position(tokens[0].toInt(), tokens[1].toInt())
    }

    fun parseCommands(s: String): List<Command> = s.map(this::parseCommand).toList()

    private fun parseCommand(c: Char): Command = when (c) {
        'l' -> TurnLeft
        'r' -> TurnRight
        'f' -> MoveForward
        'b' -> MoveBackward
        else -> UnknownCommand
    }

}