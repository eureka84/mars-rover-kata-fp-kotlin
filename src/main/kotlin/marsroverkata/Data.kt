package marsroverkata

val Nil = listOf<Nothing>()

data class Planet(val width: Int, val height: Int, val obstacles: List<Position> = Nil)
data class Position(val x: Int, val y: Int)
data class Rover(val position: Position, val direction: Direction, val planet: Planet)

enum class Command{
    TurnLeft,
    TurnRight,
    MoveForward,
    MoveBackward,
    UnknownCommand
}

enum class Direction {
    N, E, S, W
}
