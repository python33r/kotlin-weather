import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A single record from a weather station.
 *
 * A record consists of:
 * - Combined date & time
 * - Wind speed (metres per second)
 * - Air temperature (Celsius)
 * - Solar irradiance (Watts per square metre)
 * - Relative humidity (%)
 *
 * A proper date & time is always required but the meteorological
 * properties can be null in order to represent missing measurements.
 */
data class WeatherRecord(
    val time: LocalDateTime,
    val windSpeed: Double?,
    val temperature: Double?,
    val solarIrradiance: Double?,
    val humidity: Double?
) {
    companion object {
        val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    }

    /**
     * Generates a CSV-formatted string representation of this record.
     *
     * @return This record as a string
     */
    override fun toString() = buildString {
        append(time.format(TIME_FORMAT))
        append(",")
        append(windSpeed ?: "")
        append(",")
        append(temperature ?: "")
        append(",")
        append(solarIrradiance ?: "")
        append(",")
        append(humidity ?: "")
    }
}
