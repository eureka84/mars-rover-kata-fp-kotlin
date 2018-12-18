package marsroverkata

import arrow.effects.IO
import marsroverkata.Data.Command
import marsroverkata.Data.Planet
import marsroverkata.Data.Position
import marsroverkata.DataParsers.parseCommands
import marsroverkata.DataParsers.parseObstacles
import marsroverkata.DataParsers.parsePlanet
import marsroverkata.DataParsers.parsePosition
import marsroverkata.IoOps.ask
import marsroverkata.IoOps.puts

object GameInteractions {

    val welcome: () -> IO<Unit> = { puts("Welcome to the Mars Rover Kata!") }

    val readPlanet: () -> IO<Planet> = { ask("What is the size of the planet?").map(parsePlanet) }

    val readObstacles: () -> IO<List<Position>> = { ask("Where are the obstacles?").map(parseObstacles) }

    val readPosition: () -> IO<Position> = { ask("What is the position of the rover?").map(parsePosition) }

    val readCommands: () -> IO<List<Command>> = { ask("Waiting commands...").map(parseCommands) }

    val display: (Result) -> IO<Unit> = { result ->
        val (hitObstacle, rover) = result
        val prefix = if (hitObstacle) "O:" else ""
        puts("$prefix${rover.direction}:${rover.position.x},${rover.position.y}")
    }

}