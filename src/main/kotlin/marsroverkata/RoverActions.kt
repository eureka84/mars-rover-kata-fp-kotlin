package marsroverkata

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import marsroverkata.Models.Direction.*
import marsroverkata.Models.Direction
import marsroverkata.Models.Rover
import marsroverkata.Models.Position
import marsroverkata.Models.Planet

object RoverActions {

    fun rotateRight(r: Rover): Rover = r.copy(direction = rotateRight(r.direction))

    fun rotateRight(d: Direction): Direction = when (d) {
        N -> E
        E -> S
        S -> W
        W -> N
    }

    fun rotateLeft(r: Rover): Rover = r.copy(direction = rotateLeft(r.direction))

    fun rotateLeft(d: Direction): Direction = when (d) {
        N -> W
        W -> S
        S -> E
        E -> N
    }

    fun moveForward(r: Rover): Option<Position> = when (r.direction) {
        S -> moveSouth(r.position, r.planet)
        N -> moveNorth(r.position, r.planet)
        E -> moveEast(r.position, r.planet)
        W -> moveWest(r.position, r.planet)
    }

    fun moveBackward(r: Models.Rover): Option<Position> = when (r.direction) {
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