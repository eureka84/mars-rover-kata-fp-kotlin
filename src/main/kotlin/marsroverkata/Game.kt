package marsroverkata

import arrow.effects.IO
import marsroverkata.Data.Direction.N
import marsroverkata.Data.Rover
import marsroverkata.GameInteractions.display
import marsroverkata.GameInteractions.handleCommands
import marsroverkata.GameInteractions.readCommands
import marsroverkata.GameInteractions.readObstacles
import marsroverkata.GameInteractions.readPlanet
import marsroverkata.GameInteractions.readPosition
import marsroverkata.GameInteractions.welcome

object Game {

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

}