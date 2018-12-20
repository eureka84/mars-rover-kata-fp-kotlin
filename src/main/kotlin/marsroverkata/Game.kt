package marsroverkata

import arrow.effects.IO

object Game {

    fun run() {
        runIO().unsafeRunSync()
    }

    private fun runIO(): IO<Unit> =
            welcome()
                    .flatMap { readPlanet() }
                    .flatMap { planet -> readObstacles().map { obs -> planet.copy(obstacles = obs) } }
                    .flatMap { planet -> readPosition().map { pos ->
                        Rover(position = pos, direction = N, planet = planet) }
                    }
                    .flatMap { rover -> readCommands().map { cs -> handleCommands(rover, cs) } }
                    .flatMap { result -> display(result) }

}