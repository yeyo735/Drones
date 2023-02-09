class Trips {
    companion object {
        fun generate(drones: List<Drone>, locations: List<Location>): List<Map<Drone, List<List<Location>>>> {
            var control: List<Map<Drone, List<List<Location>>>> = ArrayList()
            var locationsLeft = locations.toList()
            while (drones.isNotEmpty() && locationsLeft.isNotEmpty()) {
                control = drones.map { drone ->
                    val tripsForDrone = getControlTripsForDrone(drone, control).toMutableList()
                    var locationsPicked = pickLocationsForCapacity(
                        drone.weight,
                        locationsLeft
                    )

                    if (locationsPicked.isNotEmpty()) {
                        locationsLeft =
                            locationsLeft.filter { location -> locationsPicked.none { picked -> picked == location } }
                        tripsForDrone += listOf(locationsPicked)
                    }

                    mapOf(drone to tripsForDrone)
                }
            }
            return control
        }

        private fun getControlTripsForDrone(
            drone: Drone,
            control: List<Map<Drone, List<List<Location>>>>
        ): List<List<Location>> {
            return control.asSequence().map { droneControl -> droneControl[drone] ?: emptyList() }.flatten().toList()
        }

        private fun pickLocationsForCapacity(capacity: Int, locationsLeft: List<Location>): List<Location> {
            var locationsPicked: MutableList<Location> = mutableListOf()
            if (locationsLeft.isNotEmpty())
                locationsPicked.addAll(listOf(locationsLeft.reduce { locationPicked, currentLocation ->
                    locationsPicked.add(locationPicked)
                    var locationsCurrent: MutableList<Location> = mutableListOf()
                    locationsCurrent.add(currentLocation)
                    var currentLoad = calculateLoadForLocations(locationsPicked.toList())
                    var remainingCapacity = capacity - currentLoad
                    if (currentLocation.weight < remainingCapacity && locationsLeft.size > 1) {
                        var found = pickLocationsForCapacity(
                            remainingCapacity - currentLocation.weight,
                            locationsLeft.subList(locationsLeft.indexOf(currentLocation) + 1, locationsLeft.size)
                        )
                        if (found.isNotEmpty()) {
                            locationsPicked.addAll(listOf(currentLocation))
                            locationsPicked.addAll(found)
                            return locationsPicked.toList()
                        } else {
                            locationsPicked.add(locationPicked)
                            return locationsPicked.toList()
                        }
                    } else if (currentLocation.weight <= remainingCapacity) {
                        locationsPicked.addAll(listOf(currentLocation))
                        return locationsPicked.toList()
                    } else {
                        return locationsPicked.toList()
                    }

                }))
            return locationsPicked
        }


        fun calculateLoadForLocations(location: List<Location>): Int {
            return location.sumOf { it.weight }
        }
    }
}