package marsroverkata

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.*

class GameTests {

    @Test fun example() {
        val result = execute(inputs("5x5", noObstacles, "0,0", "rrffflbb"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "E:3,3"
        )

        assertEquals(result, expected)
    }

    @Test fun `discard invalid commands and keep going`() {
        val result = execute(inputs("5x5", noObstacles, "2,3", "frzffxrbbylbll"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "W:4,4"
        )

        assertEquals(result, expected)
    }

    @Test fun `hit an obstacle`() {
        val result = execute(inputs("5x5", "0,0/2,2", "0,2", "lfff"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "O-W:0,1"
        )

        assertEquals(result, expected)
    }

    @Test fun `move forward`() {
        val result = execute(inputs("10x10", noObstacles, "5,5", "ff"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "N:3,5"
        )

        assertEquals(result, expected)
    }

    @Test fun `move backward`() {
        val result = execute(inputs("10x10", noObstacles, "5,5", "bb"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "N:7,5"
        )

        assertEquals(result, expected)
    }

    @Test fun `turn 90 to the right`() {
        val result = execute(inputs("2x2", noObstacles, "0,0", "r"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "E:0,0"
        )

        assertEquals(result, expected)
    }

    @Test fun `turn 180 to the right`() {
        val result = execute(inputs("2x2", noObstacles, "0,0", "rr"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "S:0,0"
        )

        assertEquals(result, expected)
    }

    @Test fun `turn 270 to the right`() {
        val result = execute(inputs("2x2", noObstacles, "0,0", "rrr"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "W:0,0"
        )

        assertEquals(result, expected)
    }

    @Test fun `turn 360 to the right`() {
        val result = execute(inputs("2x2", noObstacles, "0,0", "rrrr"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "N:0,0"
        )

        assertEquals(result, expected)
    }

    @Test fun `turn 90 to the left`() {
        val result = execute(inputs("2x2", noObstacles, "0,0", "l"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "W:0,0"
        )

        assertEquals(result, expected)
    }

    @Test fun `turn 180 to the left`() {
        val result = execute(inputs("2x2", noObstacles, "0,0", "ll"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "S:0,0"
        )

        assertEquals(result, expected)
    }

    @Test fun `turn 270 to the left`() {
        val result = execute(inputs("2x2", noObstacles, "0,0", "lll"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "E:0,0"
        )

        assertEquals(result, expected)
    }

    @Test fun `turn 360 to the left`() {
        val result = execute(inputs("2x2", noObstacles, "0,0", "llll"))

        val expected = outputs(
                "Welcome to the Mars Rover Kata!",
                "What is the size of the planet?",
                "Where are the obstacles?",
                "What is the position of the rover?",
                "Waiting commands...",
                "N:0,0"
        )

        assertEquals(result, expected)
    }

    private fun inputs(vararg value: String): String =
        value.joinToString(enter)

    private fun outputs(vararg value: String): String =
        value.joinToString(enter) + enter

    private fun execute(value: String): String  {
        val swapStreams = {inputStream: InputStream, printStream: PrintStream ->
            System.setIn(inputStream)
            System.setOut(printStream)
        }
        val initialOut = System.out
        val initialIn = System.`in`
        val byteArrayOutputStream = ByteArrayOutputStream()
        swapStreams(ByteArrayInputStream(value.toByteArray()), PrintStream(byteArrayOutputStream))
        Game.run()
        swapStreams(initialIn, initialOut)
        return byteArrayOutputStream.toString()
    }


    private val enter = System.getProperty("line.separator")

    private val noObstacles = ""

}