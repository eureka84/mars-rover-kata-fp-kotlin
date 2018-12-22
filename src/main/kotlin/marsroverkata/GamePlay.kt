package marsroverkata

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.syntax.collections.firstOption
import arrow.syntax.collections.tail

data class Result(val hitObstacle: Boolean, val rover: Rover)

fun handleCommands(r: Rover, cs: List<Command>): Result =
        cs.firstOption()
                .fold(
                        { Result(hitObstacle = false, rover = r) },
                        { c ->
                            handleCommand(r, c)
                                    .fold(
                                            { Result(hitObstacle = true, rover = r) },
                                            { nextRover -> handleCommands(nextRover, cs.tail()) }
                                    )
                        }
                )

private fun handleCommand(r: Rover, c: Command): Option<Rover> = when (c) {
    TurnRight -> Some(rotateRight(r))
    TurnLeft -> Some(rotateLeft(r))
    MoveForward -> moveForward(r).map { p -> r.copy(position = p) }
    MoveBackward -> moveBackward(r).map { p -> r.copy(position = p) }
    UnknownCommand -> Some(r)
}

private fun rotateRight(r: Rover): Rover = r.copy(direction = when (r.direction) {
    N -> E
    E -> S
    S -> W
    W -> N
})

private fun rotateLeft(r: Rover): Rover = r.copy(direction = when (r.direction) {
    N -> W
    W -> S
    S -> E
    E -> N
})

private fun moveForward(r: Rover): Option<Position> = when (r.direction) {
    S -> moveSouth(r.position, r.planet)
    N -> moveNorth(r.position, r.planet)
    E -> moveEast(r.position, r.planet)
    W -> moveWest(r.position, r.planet)
}

private fun moveBackward(r: Rover): Option<Position> = when (r.direction) {
    S -> moveNorth(r.position, r.planet)
    N -> moveSouth(r.position, r.planet)
    E -> moveWest(r.position, r.planet)
    W -> moveEast(r.position, r.planet)
}

private fun moveSouth(position: Position, planet: Planet): Option<Position> {
    val newX = (position.x + 1) % planet.height
    return testPositionDoesNotHitObstacle(planet, position.copy(x = newX))
}

private fun moveNorth(position: Position, planet: Planet): Option<Position> {
    val newX = if (position.x > 0) position.x - 1 else planet.height - 1
    return testPositionDoesNotHitObstacle(planet, position.copy(x = newX))
}

private fun moveEast(position: Position, planet: Planet): Option<Position> {
    val newY = (position.y + 1) % planet.width
    return testPositionDoesNotHitObstacle(planet, position.copy(y = newY))
}

private fun moveWest(position: Position, planet: Planet): Option<Position> {
    val newY = if (position.y > 0) position.y - 1 else planet.width - 1
    return testPositionDoesNotHitObstacle(planet, position.copy(y = newY))
}

private fun testPositionDoesNotHitObstacle(planet: Planet, newPosition: Position): Option<Position> {
    return if (planet.obstacles.contains(newPosition)) None
    else Some(newPosition)
}