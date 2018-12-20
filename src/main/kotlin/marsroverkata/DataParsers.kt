package marsroverkata

val parsePlanet: (String) -> Planet = { s: String ->
    val tokens = s.split("x")
    Planet(tokens[0].toInt(), tokens[1].toInt())
}

val parseObstacles: (String) -> List<Position> = { s: String ->
    if (s.isEmpty()) listOf()
    else s.split("/").map(parsePosition)
}

val parsePosition: (String) -> Position = { s: String ->
    val tokens = s.split(",")
    Position(tokens[0].toInt(), tokens[1].toInt())
}

val parseCommands: (String) -> List<Command> = { s: String -> s.map(parseCommand) }

private val parseCommand: (Char) -> Command = { c: Char ->
    when (c) {
        'l' -> Command.TurnLeft
        'r' -> Command.TurnRight
        'f' -> Command.MoveForward
        'b' -> Command.MoveBackward
        else -> Command.UnknownCommand
    }
}