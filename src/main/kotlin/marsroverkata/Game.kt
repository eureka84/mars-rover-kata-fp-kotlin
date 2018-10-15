package marsroverkata

import arrow.effects.IO
import marsroverkata.Data.Direction.N
import marsroverkata.Data.Rover
import marsroverkata.GameInteractions.welcome
import marsroverkata.GameInteractions.readPlanet
import marsroverkata.GameInteractions.readObstacles
import marsroverkata.GameInteractions.readPosition
import marsroverkata.GameInteractions.readCommands
import marsroverkata.GamePlay.handleCommands
import marsroverkata.GameInteractions.display

object Game {

    fun run() {
        runIO().unsafeRunSync()
    }

    private fun runIO(): IO<Unit> =
            welcome()
                    .flatMap { readPlanet() }
                    .flatMap { planet -> readObstacles().map { obs -> planet.copy(obstacles = obs) } }
                    .flatMap { planet -> readPosition().map { pos -> Rover(position = pos, direction = N, planet = planet) } }
                    .flatMap { rover -> readCommands().map { cs -> handleCommands(rover, cs) } }
                    .flatMap { (hit, rover) -> display(Result(hit,rover)) }

}