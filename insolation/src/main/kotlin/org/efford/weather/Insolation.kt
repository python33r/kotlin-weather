package org.efford.weather

import java.time.LocalDate
import kotlin.system.exitProcess

/**
 * Program to compute insolation from weather station data.
 *
 * A dataset filename & ISO 8601 date must be provided on the command line.
 *
 * @param[args] Array of command line arguments
 */
fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Error: filename and date required as arguments")
        exitProcess(1)
    }

    try {
        val date = LocalDate.parse(args[1])
        WeatherDataset(args[0])
            .also { displayInsolation(it, date) }
    }
    catch (error: Exception) {
        println("Error: ${error.message}")
        exitProcess(2)
    }
}

private fun displayInsolation(dataset: WeatherDataset, date: LocalDate) {
    dataset.insolation(date)?.let {
        printf("\n%d hours of measurements for %s\n\n", it.second, date)
        printf("Insolation = %.4g J/m\u00b2\n\n", it.first)
        return
    }
    println("\nNo data for $date\n")
}

private fun printf(fmt: String, vararg args: Any?) {
    print(String.format(fmt, *args))
}
