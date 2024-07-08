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
            .also { summarize(it) }
            .also { displayStatsFor(it) }
    }
    catch (error: Exception) {
        println("Error: ${error.message}")
        exitProcess(2)
    }
}

private fun summarize(dataset: WeatherDataset) {
    with(dataset) {
        println(
            """

            $size valid records, $skipped skipped

            Missing wind speed  : $missingWindSpeed
            Missing temperature : $missingTemperature
            Missing irradiance  : $missingIrradiance
            Missing humidity    : $missingHumidity
            """.trimIndent())
    }
}

private fun displayStatsFor(dataset: WeatherDataset) {
    displayMaxWindSpeed(dataset)
    dataset.minHumidity()?.let { displayHumidity("Lowest", it) }
    dataset.maxHumidity()?.let { displayHumidity("Highest", it) }
    dataset.minTemperature()?.let { displayTemperature("Lowest", it) }
    dataset.maxTemperature()?.let { displayTemperature("Highest", it) }
}

private fun displayMaxWindSpeed(dataset: WeatherDataset) {
    dataset.maxWindSpeed()?.let {
        printf("\nHighest wind speed = %.1f m/s\n", it.windSpeed)
        printf("(Measurement made at %s on %s)\n\n",
            it.time.toLocalTime(), it.time.toLocalDate())
    }
}

private fun displayHumidity(description: String, rec: WeatherRecord) {
    printf("%s humidity = %.1f%%\n", description, rec.humidity)
    printf("(Measured at %s on %s)\n\n",
        rec.time.toLocalTime(), rec.time.toLocalDate())
}

private fun displayTemperature(description: String, rec: WeatherRecord) {
    printf("%s temperature = %.1f\u00b0C\n", description, rec.temperature)
    printf("(Measured at %s on %s)\n\n",
        rec.time.toLocalTime(), rec.time.toLocalDate())
}

private fun printf(fmt: String, vararg items: Any?) {
    print(String.format(fmt, *items))
}
