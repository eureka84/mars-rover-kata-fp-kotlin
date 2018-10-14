package marsroverkata

import arrow.core.Option
import arrow.core.Some
import arrow.effects.IO
import arrow.syntax.collections.tail
import marsroverkata.Models.Command
import marsroverkata.Models.Command.*
import marsroverkata.Models.Planet
import marsroverkata.Models.Position
import marsroverkata.Models.Rover
import marsroverkata.Parsers.parseCommands
import marsroverkata.Parsers.parseObstacles
import marsroverkata.Parsers.parsePlanet
import marsroverkata.Parsers.parsePosition
import marsroverkata.RoverActions.moveBackward
import marsroverkata.RoverActions.moveForward
import marsroverkata.RoverActions.rotateLeft
import marsroverkata.RoverActions.rotateRight


object UseCases {

    fun welcome() = IoOps.puts("Welcome to the Mars Rover Kata!")

    fun readPlanet(): IO<Planet> =
            IoOps.ask("What is the size of the planet?").map { s -> parsePlanet(s) }

    fun readObstacles(): IO<List<Position>> =
            IoOps.ask("Where are the obstacles?").map { s -> parseObstacles(s) }

    fun readPosition(): IO<Position> =
            IoOps.ask("What is the position of the rover?").map { s -> parsePosition(s) }

    fun readCommands(): IO<List<Command>> =
            IoOps.ask("Waiting commands...").map { s -> parseCommands(s) }

    fun handleCommands(r: Rover, cs: List<Command>): Rover = when {
        cs.isNotEmpty() -> handleCommand(r, cs[0]).fold({ r }, { r -> handleCommands(r, cs.tail()) })
        else -> r
    }

    private fun handleCommand(r: Rover, c: Command): Option<Rover> = when (c) {
        TurnRight -> Some(rotateRight(r))
        TurnLeft -> Some(rotateLeft(r))
        MoveForward -> moveForward(r).map { p -> r.copy(position = p) }
        MoveBackward -> moveBackward(r).map { p -> r.copy(position = p) }
        UnknownCommand -> Some(r)
    }

    fun display(r: Rover): IO<Unit> = IoOps.puts("${r.direction}:${r.position.x},${r.position.y}")

}