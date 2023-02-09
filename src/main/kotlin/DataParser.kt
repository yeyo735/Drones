import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.Reader

class DataParser {
    companion object {
        fun getLocations(fileName: String?): MutableList<Location> {
            var locations: MutableList<Location> = mutableListOf()
            val csvFormat = CSVFormat.RFC4180
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
            val reader: Reader = FileReader(fileName)
            val csvParser = CSVParser(reader, csvFormat)
            csvParser.forEach {
                var location = it[0].substringBefore("]").substringAfter("[")
                var weight = it[1].substringBefore("]").substringAfter("[").toInt()
                println("$location $weight")
                locations.add(Location(location, weight))
            }
            return locations
        }

        fun getDrones(fileName: String?): MutableList<Drone> {
            var drones: MutableList<Drone> = mutableListOf()
            val inputStream: FileInputStream? = fileName?.let { File(it).inputStream() }
            val lineList = mutableListOf<String>()
            inputStream?.bufferedReader()?.forEachLine { lineList.add(it) }
            var listDrones = lineList[0]
            var dronesList: List<String> = listDrones.split(",")
            var name = ""
            var weight = 0
            for (i in dronesList) {
                val str = i.substringBefore("]").substringAfter("[")
                when {
                    !isNumeric(str) -> name = str
                    isNumeric(str) -> weight = str.toInt()
                }
                if (name.isNotBlank() && weight > 0) {
                    drones.add(Drone(name, weight))
                    name = ""
                    weight = 0
                }
            }
            if (drones.isNotEmpty())
                drones.forEach {
                    println("${it.name} ${it.weight}")
                }
            return drones
        }

        fun isNumeric(s: String): Boolean {
            return try {
                s.toInt()
                true
            } catch (e: NumberFormatException) {
                false
            }
        }

        fun writeFile(all: List<Map<Drone, List<List<Location>>>>) {
            val fileOut = "src/main/resources/Output.txt"
            val myfile = File(fileOut)
            myfile.printWriter().use { out ->
                all.forEach { trips ->
                    trips.forEach { drone ->
                        out.println()
                        out.print("[${drone.key.name}]")
                        for (trip in drone.value) {
                            out.println()
                            out.println("Trip #${drone.value.indexOf(trip) + 1}")
                            var pos = 0
                            for (location in trip) {
                                pos = +1
                                if (pos <= trip.size) {
                                    out.print("[" + location.location + "], ")
                                } else {
                                    out.print("[" + location.location + "] ")
                                    out.println()
                                }
                            }
                        }
                        out.println()
                    }
                }
            }
        }
    }
}