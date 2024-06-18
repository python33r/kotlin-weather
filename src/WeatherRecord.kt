import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A single record from a weather station.
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
