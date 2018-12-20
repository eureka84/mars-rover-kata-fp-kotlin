package marsroverkata

data class Planet(val width: Int, val height: Int, val obstacles: List<Position> = listOf())
data class Position(val x: Int, val y: Int)
data class Rover(val position: Position, val direction: Direction, val planet: Planet)

sealed class Command
object TurnLeft : Command()
object TurnRight : Command()
object MoveForward : Command()
object MoveBackward : Command()
object UnknownCommand : Command()

sealed class Direction {
    override fun toString(): String = when (this) {
        N -> "N"
        E -> "E"
        S -> "S"
        W -> "W"
    }
}

object N : Direction()
object E : Direction()
object S : Direction()
object W : Direction()
