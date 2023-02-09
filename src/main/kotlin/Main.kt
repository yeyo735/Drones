import java.io.IOException


fun main(args: Array<String>) {
    val path = System.getProperty("user.dir")
    println("Working Directory = $path")
    readFile()
}

fun readFile() {
    try {
        println("Enter location file")
        var fileName = readlnOrNull() // ../../Downloads/01/Input.txt
        var drones: MutableList<Drone> = DataParser.getDrones(fileName)
        var locations: MutableList<Location> = DataParser.getLocations(fileName)
        var all = Trips.generate(drones, locations)
        DataParser.writeFile(all)
    } catch (io: IOException) {
        println(io.message)
    }
}

