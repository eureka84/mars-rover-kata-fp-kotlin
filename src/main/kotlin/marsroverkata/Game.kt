package marsroverkata

import marsroverkata.Game.IoOps.ask
import marsroverkata.Game.IoOps.puts
import marsroverkata.Game.UseCases.Direction.N
import marsroverkata.Game.UseCases.Rover
import marsroverkata.Game.UseCases.display
import marsroverkata.Game.UseCases.handleCommands
import marsroverkata.Game.UseCases.readCommands
import marsroverkata.Game.UseCases.readObstacles
import marsroverkata.Game.UseCases.readPlanet
import marsroverkata.Game.UseCases.readPosition
import marsroverkata.Game.UseCases.welcome
import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.effects.IO
import arrow.syntax.collections.tail

class Game {

    fun run() {
        runIO().unsafeRunSync()
    }

    private fun runIO(): IO<Unit> =
            welcome()
                    .flatMap { readPlanet() }
                    .flatMap { p -> readObstacles().map { obs -> p.copy(obstacles = obs) } }
                    .flatMap { p -> readPosition().map { pos -> Rover(position = pos, direction = N, planet = p) } }
                    .flatMap { r -> readCommands().map { cs -> handleCommands(r, cs) } }
                    .flatMap { r -> display(r) }

    object UseCases {
        fun welcome() = puts("Welcome to the Mars Rover Kata!")

        fun readPlanet(): IO<Planet> =
                ask("What is the size of the planet?").map(this::parsePlanet)

        fun readObstacles(): IO<List<Position>> =
                ask("Where are the obstacles?").map(this::parseObstacles)

        fun readPosition(): IO<Position> =
                ask("What is the position of the rover?").map(this::parsePosition)

        fun readCommands(): IO<List<Command>> =
                ask("Waiting commands...").map(this::parseCommands)

        fun handleCommands(r: Rover, cs: List<Command>): Rover = when {
            cs.isNotEmpty() -> handleCommand(r, cs[0]).fold({ r }, { r -> handleCommands(r, cs.tail()) })
            else -> r
        }

        fun display(r: Rover): IO<Unit> = puts("${r.direction}:${r.position.x},${r.position.y}")

        private fun handleCommand(r: Rover, c: Command): Option<Rover> = when(c) {
            Command.TurnRight -> Some(r.copy(direction = rotateRight(r.direction)))
            Command.TurnLeft -> Some(r.copy(direction = rotateLeft(r.direction)))
            Command.MoveForward -> moveForward(r).map { p ->r.copy(position = p)}
            Command.MoveBackward -> moveBackward(r).map { p ->r.copy(position = p)}
            Command.UnknownCommand -> Some(r)
        }

        private fun rotateRight(d: Direction): Direction = when(d) {
            Direction.N -> Direction.E
            Direction.E -> Direction.S
            Direction.S -> Direction.W
            Direction.W -> Direction.N
        }

        private fun rotateLeft(d: Direction): Direction = when(d) {
            Direction.N -> Direction.W
            Direction.W -> Direction.S
            Direction.S -> Direction.E
            Direction.E -> Direction.N
        }

        private fun moveForward(r: Rover): Option<Position> = when(r.direction) {
            Direction.S -> moveSouth(r.position, r.planet)
            Direction.N -> moveNorth(r.position, r.planet)
            Direction.E -> moveEast(r.position, r.planet)
            Direction.W -> moveWest(r.position, r.planet)
        }

        private fun moveBackward(r: Rover): Option<Position> = when(r.direction) {
            Direction.S -> moveNorth(r.position, r.planet)
            Direction.N -> moveSouth(r.position, r.planet)
            Direction.E -> moveWest(r.position, r.planet)
            Direction.W -> moveEast(r.position, r.planet)
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

        private fun parsePlanet(s: String): Planet {
            val tokens = s.split("x")
            return Planet(tokens[0].toInt(), tokens[1].toInt())
        }

        private fun parseObstacles(s: String): List<Position> =
                if (s.isEmpty()) listOf()
                else s.split("/").asSequence().map(this::parsePosition).toList()

        private fun parsePosition(s: String): Position {
            val tokens = s.split(",")
            return Position(tokens[0].toInt(), tokens[1].toInt())
        }

        private fun parseCommands(s: String): List<Command> = s.map(this::parseCommand).toList()

        private fun parseCommand(c: Char): Command = when (c) {
            'l' -> Command.TurnLeft
            'r' -> Command.TurnRight
            'f' -> Command.MoveForward
            'b' -> Command.MoveBackward
            else -> Command.UnknownCommand
        }

        data class Planet(val width: Int, val height: Int, val obstacles: List<Position> = listOf())
        data class Position(val x: Int, val y: Int)
        data class Rover(val position: Position, val direction: Direction, val planet: Planet)

        sealed class Direction {
            override fun toString(): String = when(this){
                N -> "N"
                E -> "E"
                S -> "S"
                W -> "W"
            }

            object N : Direction()
            object E : Direction()
            object S : Direction()
            object W : Direction()
        }

        sealed class Command {
            object TurnLeft : Command()
            object TurnRight : Command()
            object MoveForward : Command()
            object MoveBackward : Command()
            object UnknownCommand : Command()
        }
    }

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

}