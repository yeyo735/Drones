import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class DataParserTest {

    @Test
    fun getLocationsTest() {
        val fileName = "src/main/resources/Input.txt"
        val locations = DataParser.getLocations(fileName)
        assertEquals(16, locations.size)
        assertEquals("LocationA", locations[0].location)
        assertEquals("LocationB", locations[1].location)
        assertEquals("LocationC", locations[2].location)
    }

    @Test
    fun testGetDrones() {
        // Arrange
        val fileName = "src/main/resources/Input.txt"

        // Act
        val drones = DataParser.getDrones(fileName)

        // Assert
        assertEquals(3, drones.size)
        assertEquals("DroneA", drones[0].name)
        assertEquals(200, drones[0].weight)
        assertEquals("DroneB", drones[1].name)
        assertEquals(250, drones[1].weight)
        assertNotEquals("Dron 3", drones[2].name)
    }

    @Test
    fun testIsNumeric() {
        // Arrange
        val numericString = "123"
        val nonNumericString = "abc"

        // Act and Assert
        assertTrue(DataParser.isNumeric(numericString))
        assertFalse(DataParser.isNumeric(nonNumericString))
    }

    @Test
    fun testWriteFile() {
        // Assert
        val fileContent = "src/main/resources/Output.txt"
        assertTrue(fileContent.contains("[DroneA]"))
        assertTrue(fileContent.contains("Trip #1"))
        assertTrue(fileContent.contains("[LocationA],"))
    }
}
