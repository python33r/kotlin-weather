import java.time.LocalDateTime
import kotlin.system.exitProcess

/**
 * Program entry point.
 *
 * A dataset filename must be provided on the command line.
 *
 * @param[args] Array of command line arguments
 */
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Error: filename argument required")
        exitProcess(1)
    }

    try {
        WeatherDataset(args[0])
            .also { displaySummaryOf(it) }
            .also { displayWeatherInfoFor(it) }
    }
    catch (error: Exception) {
        println("Error: ${error.message}")
        exitProcess(2)
    }
}

private fun displaySummaryOf(dataset: WeatherDataset) {
    with(dataset) {
        println("\n$size valid records, $skipped skipped\n")
        println("Missing wind speed  : $missingWindSpeed")
        println("Missing temperature : $missingTemperature")
        println("Missing irradiance  : $missingIrradiance")
        println("Missing humidity    : $missingHumidity")
    }
}

private fun displayWeatherInfoFor(dataset: WeatherDataset) {
    with(System.out) {
        with(dataset) {
            maxWindSpeed()?.let {
                printf("\nHighest wind speed = %.1f m/s\n", it.windSpeed)
                displayTime(it.time)
            }
            minHumidity()?.let {
                printf("Lowest humidity = %.1f%%\n", it.humidity)
                displayTime(it.time)
            }
            maxTemperature()?.let {
                printf("Highest temperature = %.1f\u00b0C\n", it.temperature)
                displayTime(it.time)
                val date = it.time.toLocalDate()
                insolation(date)?.let { result ->
                    printf("Insolation on %s: %.4g J/m\u00b2 (%d hours)\n",
                        date, result.first, result.second)
                }
            }
        }
    }
}

private fun displayTime(time: LocalDateTime) {
    with(time) {
        System.out.printf("(measured on %s at %s)\n\n", toLocalDate(), toLocalTime())
    }
}
