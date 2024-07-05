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

private fun displayWeatherInfoFor(dataset: WeatherDataset) {
    displayMaxWindSpeed(dataset)
    displayHumidities(dataset)
    displayTempsAndInsolations(dataset)
}

private fun displayMaxWindSpeed(dataset: WeatherDataset) {
    dataset.maxWindSpeed()?.let {
        printf("\nHighest wind speed = %.1f m/s\n", it.windSpeed)
        printf("(Measurement made at %s)\n\n", it.time)
    }
}

private fun displayHumidities(dataset: WeatherDataset) {
    dataset.minHumidity()?.let {
        printf("Lowest humidity = %.1f%%\n", it.humidity)
        printf("(Measured at %s)\n\n", it.time)
    }
    dataset.maxHumidity()?.let {
        printf("Highest humidity = %.1f%%\n", it.humidity)
        printf("(Measured at %s)\n\n", it.time)
    }
}

private fun displayTempsAndInsolations(dataset: WeatherDataset) {
    dataset.minTemperature()?.let {
        printf("Lowest temperature = %.1f\u00b0C\n", it.temperature)
        printf("(Measured at %s)\n", it.time)
        val date = it.time.toLocalDate()
        dataset.insolation(date)?.let { result ->
            printf("Insolation on %s = %.4g J/m\u00b2\n\n", date, result.first)
        }
    }
    dataset.maxTemperature()?.let {
        printf("Highest temperature = %.1f\u00b0C\n", it.temperature)
        printf("(Measured at %s)\n", it.time)
        val date = it.time.toLocalDate()
        dataset.insolation(date)?.let { result ->
            printf("Insolation on %s = %.4g J/m\u00b2\n\n", date, result.first)
        }
    }
}

private fun printf(fmt: String, vararg items: Any?) {
    print(String.format(fmt, *items))
}
