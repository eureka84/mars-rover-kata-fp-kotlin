package marsroverkata

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.effects.IO
import arrow.syntax.collections.tail

import marsroverkata.Models.Rover
import marsroverkata.Models.Position
import marsroverkata.Models.Planet
import marsroverkata.Models.Command
import marsroverkata.Models.Command.MoveBackward
import marsroverkata.Models.Command.MoveForward
import marsroverkata.Models.Command.TurnLeft
import marsroverkata.Models.Command.TurnRight
import marsroverkata.Models.Command.UnknownCommand
import marsroverkata.Models.Direction
import marsroverkata.Models.Direction.N
import marsroverkata.Models.Direction.E
import marsroverkata.Models.Direction.S
import marsroverkata.Models.Direction.W


object UseCases {

    fun welcome() = IoOps.puts("Welcome to the Mars Rover Kata!")

    fun readPlanet(): IO<Planet> =
            IoOps.ask("What is the size of the planet?").map(Parsers::parsePlanet)

    fun readObstacles(): IO<List<Position>> =
            IoOps.ask("Where are the obstacles?").map(Parsers::parseObstacles)

    fun readPosition(): IO<Position> =
            IoOps.ask("What is the position of the rover?").map(Parsers::parsePosition)

    fun readCommands(): IO<List<Command>> =
            IoOps.ask("Waiting commands...").map(Parsers::parseCommands)

    fun handleCommands(r: Rover, cs: List<Command>): Rover = when {
        cs.isNotEmpty() -> handleCommand(r, cs[0]).fold({ r }, { r -> handleCommands(r, cs.tail()) })
        else -> r
    }

    fun display(r: Rover): IO<Unit> = IoOps.puts("${r.direction}:${r.position.x},${r.position.y}")

    private fun handleCommand(r: Rover, c: Command): Option<Rover> = when(c) {
        TurnRight -> Some(r.copy(direction = rotateRight(r.direction)))
        TurnLeft -> Some(r.copy(direction = rotateLeft(r.direction)))
        MoveForward -> moveForward(r).map { p ->r.copy(position = p)}
        MoveBackward -> moveBackward(r).map { p ->r.copy(position = p)}
        UnknownCommand -> Some(r)
    }

    private fun rotateRight(d: Direction): Direction = when(d) {
        N -> E
        E -> S
        S -> W
        W -> N
    }

    private fun rotateLeft(d: Direction): Direction = when(d) {
        N -> W
        W -> S
        S -> E
        E -> N
    }

    private fun moveForward(r: Rover): Option<Position> = when(r.direction) {
        S -> moveSouth(r.position, r.planet)
        N -> moveNorth(r.position, r.planet)
        E -> moveEast(r.position, r.planet)
        W -> moveWest(r.position, r.planet)
    }

    private fun moveBackward(r: Rover): Option<Position> = when(r.direction) {
        S -> moveNorth(r.position, r.planet)
        N -> moveSouth(r.position, r.planet)
        E -> moveWest(r.position, r.planet)
        W -> moveEast(r.position, r.planet)
    }

    private fun moveSouth(position: Position, planet: Planet): Option<Position> {
        val newX = (position.x + 1) % planet.height
        return confirmPositionDoesNotHitObstacle(planet, position.copy(x = newX))
    }

    private fun moveNorth(position: Position, planet: Planet): Option<Position> {
        val newX = if (position.x > 0) position.x - 1 else planet.height - 1
        return confirmPositionDoesNotHitObstacle(planet, position.copy(x = newX))
    }

    private fun moveEast(position: Position, planet: Planet): Option<Position> {
        val newY = (position.y + 1) % planet.width
        return confirmPositionDoesNotHitObstacle(planet, position.copy(y = newY))
    }

    private fun moveWest(position: Position, planet: Planet): Option<Position> {
        val newY = if (position.y > 0) position.y - 1 else planet.width - 1
        return confirmPositionDoesNotHitObstacle(planet, position.copy(y = newY))
    }

    private fun confirmPositionDoesNotHitObstacle(planet: Planet, newPosition: Position): Option<Position> {
        return if (planet.obstacles.contains(newPosition)) None
        else Some(newPosition)
    }

}