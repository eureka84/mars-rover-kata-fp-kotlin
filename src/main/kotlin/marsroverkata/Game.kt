package marsroverkata

import marsroverkata.Models.Rover
import marsroverkata.Models.Direction.N
import marsroverkata.UseCases.display
import marsroverkata.UseCases.handleCommands
import marsroverkata.UseCases.readCommands
import marsroverkata.UseCases.readObstacles
import marsroverkata.UseCases.readPlanet
import marsroverkata.UseCases.readPosition
import marsroverkata.UseCases.welcome
import arrow.effects.IO

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

}