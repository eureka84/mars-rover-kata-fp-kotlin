package marsroverkata

import arrow.core.Option
import arrow.core.Some
import arrow.effects.IO
import arrow.syntax.collections.tail
import marsroverkata.Data.Command
import marsroverkata.Data.Command.*
import marsroverkata.Data.Planet
import marsroverkata.Data.Position
import marsroverkata.Data.Rover
import marsroverkata.Parsers.parseCommands
import marsroverkata.Parsers.parseObstacles
import marsroverkata.Parsers.parsePlanet
import marsroverkata.Parsers.parsePosition
import marsroverkata.RoverActions.moveBackward
import marsroverkata.RoverActions.moveForward
import marsroverkata.RoverActions.rotateLeft
import marsroverkata.RoverActions.rotateRight
import marsroverkata.IoOps.ask
import marsroverkata.IoOps.puts


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

    fun handleCommands(r: Rover, cs: List<Command>): Rover = when {
        cs.isNotEmpty() -> handleCommand(r, cs[0]).fold({ r }, { nextRover -> handleCommands(nextRover, cs.tail()) })
        else -> r
    }

    private fun handleCommand(r: Rover, c: Command): Option<Rover> = when (c) {
        TurnRight -> Some(rotateRight(r))
        TurnLeft -> Some(rotateLeft(r))
        MoveForward -> moveForward(r).map { p -> r.copy(position = p) }
        MoveBackward -> moveBackward(r).map { p -> r.copy(position = p) }
        UnknownCommand -> Some(r)
    }

    fun display(r: Rover): IO<Unit> = puts("${r.direction}:${r.position.x},${r.position.y}")

}