package marsroverkata

import arrow.effects.IO
import marsroverkata.Data.Command
import marsroverkata.Data.Planet
import marsroverkata.Data.Position
import marsroverkata.IoOps.ask
import marsroverkata.IoOps.puts
import marsroverkata.DataParsers.parseCommands
import marsroverkata.DataParsers.parseObstacles
import marsroverkata.DataParsers.parsePlanet
import marsroverkata.DataParsers.parsePosition


object GameInteractions {

    fun welcome() = puts("Welcome to the Mars Rover Kata!")

    fun readPlanet(): IO<Planet> =
            ask("What is the size of the planet?").map { s -> parsePlanet(s) }

    fun readObstacles(): IO<List<Position>> =
            ask("Where are the obstacles?").map { s -> parseObstacles(s) }

    fun readPosition(): IO<Position> =
            ask("What is the position of the rover?").map { s -> parsePosition(s) }

    fun readCommands(): IO<List<Command>> =
            ask("Waiting commands...").map { s -> parseCommands(s) }

    fun display(result: Result): IO<Unit> {
        val (hitObstacle, rover) = result
        val prefix = if (hitObstacle) "O-" else ""
        return puts("$prefix${rover.direction}:${rover.position.x},${rover.position.y}")
    }

}